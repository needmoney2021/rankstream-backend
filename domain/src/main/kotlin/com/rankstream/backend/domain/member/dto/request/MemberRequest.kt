package com.rankstream.backend.domain.member.dto.request

import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.member.enums.MemberPosition
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MemberSearchRequest(
    @field:Size(min = 5, max = 50, message = "Id must be between 5 and 50 characters")
    val id: String?,

    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String?,

    val gender: Gender?
)

data class MemberRegistrationRequest(
    @field:NotBlank(message = "Id must not be blank")
    @field:Size(min = 5, max = 50, message = "Id must be between 5 and 50 characters")
    val id: String,

    @field:NotBlank(message = "Name must not be blank")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,

    @field:NotNull(message = "Gender must not be null")
    val gender: Gender,

    val recommenderId: String?,

    val sponsorId: String?,

    val position: MemberPosition?
)
