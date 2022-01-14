package com.example.managementtool.repository

import com.example.managementtool.model.Employee
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long> {

    @Modifying
    @Query(
        "UPDATE employee " +
                "SET supervisor_id = :supervisorId " +
                "WHERE id = :employeeId AND (SELECT count(eee.id) FROM employee AS eee WHERE eee.id = :supervisorId AND eee.supervisor_id = :employeeId) = 0",
        nativeQuery = true
    )
    fun updateEmployeeSupervisor(@Param("employeeId") employeeId: Long, @Param("supervisorId") supervisorId: Long): Int?

    @Query(
        "SELECT count(*) " +
                "FROM employee " +
                "WHERE supervisor_id IS NULL",
        nativeQuery = true
    )
    fun findMostSeniorEmployeeCount(): Int

    // You cannot insert null in nullable column from numeric type !!!
   /* @Modifying
    @Query(
        value = "INSERT INTO Employee (name, supervisor_id) " +
                "VALUES (:name, :supervisorId) " +
                "ON CONFLICT DO NOTHING",
        nativeQuery = true
    )

    fun saveOnConflictDoNothing(@Param("name") name: String, @Param("supervisorId") supervisorId: Long)*/
}