package com.example.managementtool

import com.example.managementtool.model.Employee
import com.example.managementtool.repository.EmployeeRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [ManagementToolApplication::class])
class SpringBootJPAIntegrationTest {

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Test
    fun generateBasicData() {
        val employee: Employee = employeeRepository
            .save(Employee().apply { name = "Some name" })

        val foundEmployee: Optional<Employee> = employeeRepository
            .findById(employee.id ?: 0)

        assertNotNull(foundEmployee)
        assertEquals(employee.name, foundEmployee.get().name)
    }

}