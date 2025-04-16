package com.rankstream.backend.domain.admin.dto.response

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.enums.State
import java.time.LocalDateTime

data class SigninResponse(
    val userId: String,
    val companyIdx: Long,
    val accessToken: String,
) {
    companion object {
        fun fromEntityAndToken(administrator: Administrator, accessToken: String): SigninResponse {
            with (administrator) {
                return SigninResponse(
                    userId = userId,
                    companyIdx = company.idx!!,
                    accessToken = accessToken,
                )
            }
        }
    }
}

data class AdministratorSearchResponse(
    val companyName: String,
    val id: String,
    val name: String,
    val department: String,
    val state: State,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String
)
