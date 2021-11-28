package com.example.managementtool.service

import com.example.managementtool.dto.EmployeeDTO
import com.example.managementtool.exception.AmbiguousHierarchyException
import com.example.managementtool.model.Employee
import com.example.managementtool.repository.EmployeeNativeRepository
import com.example.managementtool.repository.EmployeeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.sqlite.SQLiteException
import java.sql.SQLException

@Service
class EmployeeService(private val employeeNativeRepository: EmployeeNativeRepository) {

    var logger: Logger = LoggerFactory.getLogger(EmployeeService::class.java)

    fun getEmployeeHierarchy(): EmployeeDTO {
        val employeeList = employeeNativeRepository.findAll()
        // 'supervisor' equal null mean the most senior employee
        return buildEmployeeHierarchy(employeeList.toList(), null, EmployeeDTO())
    }

    @Throws(SQLException::class)
    @Transactional(rollbackFor = [SQLException::class])
    fun addEmployees(employeeNameSupervisorNameMap: Map<String, String>?) {
        val employeeIdSupervisorIdMap: MutableMap<Int, Int> = HashMap()
        // Add all employee
        employeeNameSupervisorNameMap?.forEach {
            // Save employee
            var employee = Employee()
            employee.name = it.key
            employee = employeeNativeRepository.save(employee)

            // Save supervisor
            var supervisor = Employee()
            supervisor.name = it.value
            supervisor = employeeNativeRepository.save(supervisor)

            if (employee.id != null && supervisor.id != null) {
                employeeIdSupervisorIdMap[employee.id!!] = supervisor.id!!
            }
        }

       // Update employee's supervisors,
        employeeIdSupervisorIdMap.forEach {
            if (employeeNativeRepository.updateEmployeeSupervisor(it.key, it.value) == 0) {
                throw SQLException("The hierarchy of the employees the user is trying to insert is ambiguous. Some of the employees are sets as supervisors of each other.")
            }
        }

        // Check if there is more than one most senior employee
        if (employeeNativeRepository.findMostSeniorEmployeeCount() > 1) {
            throw SQLException("There is more than one most senior employee")
        }
    }

    private fun buildEmployeeHierarchy(employeeList: List<Employee>, supervisorId: Int?, employeeDTO: EmployeeDTO): EmployeeDTO {
        employeeList.forEach {
            if (it.supervisorId == supervisorId) {
                employeeDTO.employeeSupervisorMap[it.name!!] = buildEmployeeHierarchy(employeeList, it.id, EmployeeDTO())
            }
        }

        return employeeDTO
    }
}