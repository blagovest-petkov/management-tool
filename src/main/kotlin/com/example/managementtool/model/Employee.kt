package com.example.managementtool.model

import org.hibernate.annotations.SQLInsert
import javax.persistence.*

@Entity
@Table(name = "employee")
// The order of the column is important and the id must be included. Hibernate firstly generate the id and after that insert it.
@SQLInsert(sql = "INSERT INTO employee (name, supervisor_id, id) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")
class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    var id: Long? = null

    @Column(name = "name", unique = true)
    var name: String? = null

    @Column(name = "supervisor_id", nullable = true)
    var supervisorId: Long? = null

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
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (supervisorId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Employee(id=$id, name=$name, supervisorId=$supervisorId)"
    }
}