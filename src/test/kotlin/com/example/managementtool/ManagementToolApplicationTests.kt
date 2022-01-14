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
class ManagementToolApplicationTests {

    @LocalServerPort
    private var port: Int = 0

    /*@Autowired
    private lateinit var testRestTemplate: TestRestTemplate*/

    @Autowired
    private lateinit var mockMvc: MockMvc

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

    @Test
    fun  testGetEmployeeWithMock() {
        this.mockMvc.perform(get("/employee").with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "12345678")))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("")))
    }


}
