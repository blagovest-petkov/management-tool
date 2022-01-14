package com.example.managementtool

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.web.servlet.MockMvc
import org.hamcrest.Matchers.containsString
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ManagementToolApplicationTests(
    @LocalServerPort
    private val port: Int,
    @Autowired
    private val mockMvc: MockMvc) {


    /*@Autowired
    private lateinit var testRestTemplate: TestRestTemplate*/


    @Test
    fun contextLoads() {
    }

    /*@Test
    fun testGetEmployees() {
        assertThat(
            this.testRestTemplate.getForObject(
                "http://localhost:$port/employee",
                String::class.java
            )
        ).isEmpty()
    }*/

    /**
     * On empty db has to return 200 OK with empty body.
     */
    @Test
    fun testGetEmployeeWithMock() {
        this.mockMvc.perform(get("/employee").with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "12345678")))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("")))
    }

    /**
     * The happy path with proper hierarchy.
     */
    @Test
    fun testPostEmployeeProperHierarchy() {

    }

    /**
     * Test if some employees are supervisor of each other.
     * An exception has to be thrown.
     */
    @Test
    fun testPostEmployeeAmbiguousHierarchy() {

    }


    /**
     * Test if there is more than one most senior supervisor.
     * An exception has to be thrown.
     */
    fun testPostEmployeeWithMoreThanOneMostSeniorSupervisor() {

    }


}
