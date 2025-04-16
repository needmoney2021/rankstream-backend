package com.rankstream.backend.domain.admin.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SigninRequest(
    @field:NotBlank(message = "Email must not be blank")
    @field:Pattern(
        regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$",
        message = "Email format is invalid"
    )
    @field:Size(min = 10, max = 50, message = "Email must be between 20 and 50 characters")
    val email: String,

    @field:NotBlank(message = "Password must not be blank")
    val password: String
)
