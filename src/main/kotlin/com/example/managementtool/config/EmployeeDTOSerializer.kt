package com.example.managementtool.config

import com.example.managementtool.dto.EmployeeDTO
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EmployeeDTOSerializer(t: Class<EmployeeDTO>?) : StdSerializer<EmployeeDTO>(t) {

    var logger: Logger = LoggerFactory.getLogger(EmployeeDTOSerializer::class.java)

    override fun serialize(employeeDTO: EmployeeDTO?, jsonGenerator: JsonGenerator?, serializerProvider: SerializerProvider?) {
        if (employeeDTO?.employeeSupervisorMap.isNullOrEmpty()) {
            logger.debug("There is nothing to serialize")
            return

        } else {
            jsonGenerator?.writeStartObject()
            getFormattedEmployeeHierarchy(employeeDTO, jsonGenerator)
            jsonGenerator?.writeEndObject()
        }
    }

    private fun getFormattedEmployeeHierarchy(employeeDTO: EmployeeDTO?, jsonGenerator: JsonGenerator?) {
        employeeDTO?.employeeSupervisorMap?.forEach {
            jsonGenerator?.writeObjectFieldStart(it.key)
            if (it.value.employeeSupervisorMap.isNotEmpty()) {
                getFormattedEmployeeHierarchy(it.value, jsonGenerator)
            }
            jsonGenerator?.writeEndObject()
        }
    }
}