package com.example.managementtool.repository

import com.example.managementtool.model.Employee
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface EmployeeRepository {

    @Modifying
    @Query(
        "UPDATE Employee " +
            "SET supervisor_id = (SELECT ee.id FROM employee AS ee WHERE ee.id = :supervisorId) " +
            "WHERE id = :employeeId " +
            "AND (SELECT count(eee.id) FROM Employee AS eee WHERE eee.id = :supervisorId AND eee.supervisor_id = :employeeId) = 0",
        nativeQuery = true
    )
    fun updateEmployeeSupervisor(@Param("employeeId") employeeId: Int, @Param("supervisorId") supervisorId: Int): Int

    @Query(
        "SELECT e " +
            "FROM Employee AS e " +
            "WHERE e.supervisor IS NULL",
    )
    fun findMostSeniorEmployee(): List<Employee>
}