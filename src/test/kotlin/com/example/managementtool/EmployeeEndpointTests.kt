package com.example.managementtool

import com.example.managementtool.repository.EmployeeRepository
import com.example.managementtool.service.EmployeeService
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeEndpointTests(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val employeeRepository: EmployeeRepository) {

    var logger: Logger = LoggerFactory.getLogger(EmployeeService::class.java)

    companion object {
        private const val EMPLOYEE_URL = "/employee"
    }

    @BeforeEach
    fun clearTable() {
        employeeRepository.deleteAll()
    }

    /**
     * On empty db has to return 200 OK with empty body.
     */
    @Test
    fun testGetEmployeeWithMock() {
        logger.info("testGetEmployeeWithMock")
        executeGetRequest("")
    }

    /**
     * The happy path with proper hierarchy.
     */
    @Test
    fun testPostEmployeeProperHierarchy() {
        logger.info("testPostEmployeeProperHierarchy")
        val body = "{\"Pete\":\"Nick\", \"Nick\":\"Sophie\"}"
        this.mockMvc.perform(post(EMPLOYEE_URL)
            .with(getSecurity())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andExpect(status().isCreated)

        executeGetRequest("{\"Sophie\":{\"Nick\":{\"Pete\":{}}}}")
    }

    /**
     * Test if some employees are supervisor of each other.
     * An exception has to be thrown.
     */
    @Test
    fun testPostEmployeeAmbiguousHierarchy() {
        logger.info("testPostEmployeeAmbiguousHierarchy")
        val body = "{\"Pete\":\"Nick\",\"Nick\":\"Pete\"}"
        this.mockMvc.perform(post(EMPLOYEE_URL)
            .with(getSecurity())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(Matchers.containsString("The hierarchy of the employees the user is trying to insert is ambiguous. Some employees are sets as supervisors of each other.")))

        executeGetRequest("")
    }

    /**
     * Test if there is more than one most senior supervisor.
     * An exception has to be thrown.
     */
    @Test
    fun testPostEmployeeWithMoreThanOneMostSeniorSupervisor() {
        logger.info("testPostEmployeeWithMoreThanOneMostSeniorSupervisor")
        val body = "{\"Pete\":\"Nick\",\"Barbara\":\"Sophie\"}"
        this.mockMvc.perform(post(EMPLOYEE_URL)
            .with(getSecurity())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(Matchers.containsString("There is more than one most senior employee")))

        executeGetRequest("")
    }

    private fun getSecurity(): RequestPostProcessor {
        return SecurityMockMvcRequestPostProcessors.httpBasic("user", "12345678")
    }

    fun executeGetRequest(responseBody: String) {
        this.mockMvc.perform(get(EMPLOYEE_URL).with(getSecurity()))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(Matchers.containsString(responseBody)))
    }
}