package com.example.managementtool.dto

import com.example.managementtool.dto.serializer.EmployeeDTOSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = EmployeeDTOSerializer::class)
class EmployeeDTO {
    var employeeSupervisorMap: MutableMap<String, EmployeeDTO> = HashMap()

    override fun toString(): String {
        return "EmployeeDto(employeeSupervisorMap=$employeeSupervisorMap)"
    }
}