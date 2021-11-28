package com.example.managementtool.controller

import com.example.managementtool.dto.EmployeeDTO
import com.example.managementtool.service.EmployeeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.SQLException

@RestController
@RequestMapping("/employee")
class EmployeeController(val employeeService: EmployeeService) {

    var logger: Logger = LoggerFactory.getLogger(EmployeeController::class.java)

    @GetMapping("/test")
    fun getTestMessage(): String {
        return "This is a test message"
    }

    @GetMapping
    @ResponseBody
    fun getEmployeeHierarchy(): EmployeeDTO {
        return employeeService.getEmployeeHierarchy()
    }

    @PostMapping
    fun addEmployees(@RequestBody employeeSupervisorMap: Map<String, String>?): ResponseEntity<String> {
        try {
            employeeService.addEmployees(employeeSupervisorMap)
            return ResponseEntity.status(HttpStatus.CREATED).body("Employees added successfully")

        } catch (e: SQLException) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }
}