package com.example.managementtool.repository

import com.example.managementtool.dto.EmployeeDTO
import com.example.managementtool.model.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*
import javax.swing.tree.MutableTreeNode
import kotlin.collections.ArrayList


@Repository
class EmployeeNativeRepository(
    private val entityManager: EntityManager
) {

    fun findAll(): MutableList<Employee> {
        val result: MutableList<Any?> = entityManager.createNativeQuery("SELECT * FROM employee")
            .resultList

        val employeeList: MutableList<Employee> = ArrayList()
        result.forEach {
            employeeList.add(Employee(it as Array<Object>))
        }

        return employeeList
    }

    fun findByName(name: String): Employee {
        val result: Any? = entityManager.createNativeQuery("SELECT * FROM employee WHERE name = :name")
            .setParameter("name", name)
            .singleResult

        return Employee(result as Array<Object>)
    }

    fun save(employee: Employee): Employee {
        entityManager
            .createNativeQuery("INSERT INTO employee (name, supervisor_id) VALUES (:name, :supervisorId)")
            .setParameter("name", employee.name)
            .setParameter("supervisorId", employee.supervisorId)
            .executeUpdate()

        return findByName(employee.name!!)
    }

    fun updateEmployeeSupervisor(employeeId: Int, supervisorId: Int): Int {
        return entityManager
            .createNativeQuery(
                "UPDATE employee " +
                    "SET supervisor_id = (SELECT ee.id FROM employee AS ee WHERE ee.id = :supervisorId) " +
                    "WHERE id = :employeeId " +
                    "AND (SELECT count(eee.id) FROM employee AS eee WHERE eee.id = :supervisorId AND eee.supervisor_id = :employeeId) = 0"
            )
            .setParameter("employeeId", employeeId)
            .setParameter("supervisorId", supervisorId)
            .executeUpdate()
    }

    fun findMostSeniorEmployeeCount(): Int {
        val result: MutableList<Any?> = entityManager.createNativeQuery("SELECT * FROM employee WHERE supervisor_id IS NULL")
            .resultList

        return result.size
    }
}