package com.example.managementtool

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class ManagementToolApplication

fun main(args: Array<String>) {
	runApplication<ManagementToolApplication>(*args)
}
