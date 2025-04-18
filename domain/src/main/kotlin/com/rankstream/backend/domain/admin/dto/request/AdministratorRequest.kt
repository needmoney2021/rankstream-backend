package com.rankstream.backend.domain.admin.dto.request

import com.rankstream.backend.domain.enums.State
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

data class AdministratorSearchRequest(
    val name: String?,
    val id: String?,
    val state: State?,
    val companyIdx: Long
)

data class AdministratorUpdateRequest(
    val state: State?,

    val department: String?,

    @field:Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    val newPassword: String?
)

data class AdministratorUpdateCommand(
    val state: State?,
    val department: String?,
    val newEncodedPassword: String?
)

data class AdministratorRegistrationRequest(
    @field:NotBlank(message = "Id must not be blank")
    @field:Pattern(
        regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$",
        message = "Id format is invalid"
    )
    @field:Size(min = 10, max = 50, message = "Id must be between 20 and 50 characters")
    val id: String,

    @field:NotBlank(message = "Password must not be blank")
    @field:Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    val password: String,

    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotBlank(message = "Department must not be blank")
    val department: String
)

data class AdministratorRegistrationCommand(
    val id: String,
    val encodedPassword: String,
    val name: String,
    val department: String
)
