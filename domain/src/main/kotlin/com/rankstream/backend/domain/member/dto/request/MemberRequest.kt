package com.rankstream.backend.domain.member.dto.request

import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.grade.entity.Grade
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.enums.MemberPosition
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

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
    @field:Size(min = 2, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,

    @field:NotNull(message = "Gender must not be null")
    val gender: Gender,

    @field:NotNull(message = "Grade Index must not be null")
    @field:Positive(message = "Grade Index must be positive")
    val gradeIdx: Long,

    @field:Size(min = 5, max = 50, message = "Id must be between 5 and 50 characters")
    val recommenderId: String?,

    @field:Size(min = 5, max = 50, message = "Id must be between 5 and 50 characters")
    val sponsorId: String?,

    val position: MemberPosition?,

    val joinedAt: LocalDateTime? = null,

    val isGenesis: Boolean = false,
)


data class MemberUpdateRequest(
    val state: State?,

    @field:Positive(message = "Grade Index must be positive")
    val gradeIdx: Long?
)
