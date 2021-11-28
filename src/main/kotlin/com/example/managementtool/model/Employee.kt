package com.example.managementtool.model

import java.util.*

class Employee {

    constructor()

    constructor(result: Array<Object>) {

        id = result[0] as Int
        name = result[1] as String
        supervisorId = result[2] as Int
    }

    var id: Int? = null

    var name: String? = null

    var supervisorId: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (id != other.id) return false
        if (name != other.name) return false
        if (supervisorId != other.supervisorId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (supervisorId ?: 0)
        return result
    }

    override fun toString(): String {
        return "Employee(id=$id, name=$name, supervisorId=$supervisorId)"
    }
}