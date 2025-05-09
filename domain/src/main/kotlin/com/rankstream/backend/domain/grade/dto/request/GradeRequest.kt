package com.rankstream.backend.domain.grade.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class GradeUpdateRequest(

    val name: String?,

    @field:Positive
    val achievementPoint: Double?,

    @field:Positive
    val refundRate: Double?
)

data class GradeRegistrationRequest(

    @field:NotBlank("Code must not be blank")
    val code: String,

    @field:NotBlank("Name must not be blank")
    val name: String,

    @field:Positive
    val achievementPoint: Double,

    @field:Positive
    val refundRate: Double
)


