package com.example.managementtool.service

import com.example.managementtool.dto.EmployeeDTO
import com.example.managementtool.model.Employee
import com.example.managementtool.repository.EmployeeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    var logger: Logger = LoggerFactory.getLogger(EmployeeService::class.java)

    fun getEmployeeHierarchy(): EmployeeDTO {
        val employeeList = employeeRepository.findAll()
        // 'supervisor' equal null mean the most senior employee
        return buildEmployeeHierarchy(employeeList.toList(), null, EmployeeDTO())
    }

    @Throws(SQLException::class)
    @Transactional(rollbackFor = [SQLException::class])
    fun addEmployees(employeeNameSupervisorNameMap: Map<String, String>?) {
        val employeeNameToIdMap: MutableMap<String, Long> = HashMap()
        // Add all employee
        employeeNameSupervisorNameMap?.forEach {
            // Save employee
            // If there is conflict saveOnConflictDoNothing() return an object with the id of the non-existing object
            val employee = employeeRepository.save(Employee().apply { name = it.key })

            // Save supervisor
            // If there is conflict saveOnConflictDoNothing() return an object with the id of the non-existing object
            val supervisor = employeeRepository.save(Employee().apply { name = it.value })

            if (!employeeNameToIdMap.containsKey(employee.name)) {
                employeeNameToIdMap[employee.name!!] = employee.id!!
            }
            if (!employeeNameToIdMap.containsKey(supervisor.name)) {
                employeeNameToIdMap[supervisor.name!!] = supervisor.id!!
            }
        }

        // Update employee's supervisors
        employeeNameSupervisorNameMap?.forEach {
            val employeeId = employeeNameToIdMap[it.key]!!
            val supervisorId = employeeNameToIdMap[it.value]!!

            // If an update did not happen this mean there is an ambiguous hierarchy
            if (employeeRepository.updateEmployeeSupervisor(employeeId, supervisorId) == 0) {
                throw SQLException("The hierarchy of the employees the user is trying to insert is ambiguous. Some employees are sets as supervisors of each other.")
            }
        }

        // Check if there is more than one most senior employee
        if (employeeRepository.findMostSeniorEmployeeCount() > 1) {
            throw SQLException("There is more than one most senior employee")
        }
    }

    private fun buildEmployeeHierarchy(employeeList: List<Employee>, supervisorId: Long?, employeeDTO: EmployeeDTO): EmployeeDTO {
        employeeList.forEach {
            if ((supervisorId == null && it.supervisorId == null) || it.supervisorId?.equals(supervisorId) == true) {
                employeeDTO.employeeSupervisorMap[it.name!!] =
                    buildEmployeeHierarchy(employeeList, it.id, EmployeeDTO())
            }
        }

        return employeeDTO
    }
}