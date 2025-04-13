package com.rankstream.backend.internalapi.controller.csrf

import org.hamcrest.Matchers.emptyOrNullString
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CsrfControllerTest(
    private val mockMvc: MockMvc
) {

    @Test
    @DisplayName("CSRF 요청 시 쿠키에 포함")
    fun csrfTest() {
        this.mockMvc.get("/csrf")
            .andExpect {
                status { isNoContent() }
                cookie {
                    exists("XSRF-TOKEN")
                    value("XSRF-TOKEN", not(emptyOrNullString()))
                }
            }
            .andDo { print() }
    }
}
