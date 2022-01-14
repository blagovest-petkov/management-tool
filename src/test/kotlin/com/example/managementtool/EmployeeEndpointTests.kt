package com.example.managementtool

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
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
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeEndpointTests(@Autowired private val mockMvc: MockMvc) {

    companion object {
        private const val EMPLOYEE_URL = "/employee"
    }

    /**
     * On empty db has to return 200 OK with empty body.
     */
    @Test
    fun testGetEmployeeWithMock() {
        executeGetRequest("")
    }

    /**
     * The happy path with proper hierarchy.
     */
    @Test
    fun testPostEmployeeProperHierarchy() {
        val body = "{\"Pete\":\"Nick\"}"
        this.mockMvc.perform(post(EMPLOYEE_URL)
            .with(getSecurity())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andExpect(status().isOk)

        executeGetRequest(body)
    }

    /**
     * The happy path with proper hierarchy.
     */
    @Test
    fun testPostEmployeeProperHierarchyWithOnConflictDoNothing() {
        val body = "{\"Pete\":\"Nick\", \"Barbara\":\"Nick\", \"Nick\":\"Sophie\", \"Sophie\":\"Jonas\"}"
        this.mockMvc.perform(post(EMPLOYEE_URL)
            .with(getSecurity())
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andExpect(status().isOk)

        executeGetRequest(body)
    }

    /**
     * Test if some employees are supervisor of each other.
     * An exception has to be thrown.
     */
    @Test
    fun testPostEmployeeAmbiguousHierarchy() {
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