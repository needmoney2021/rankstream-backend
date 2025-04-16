package com.rankstream.backend.domain.admin.dto.response

import com.rankstream.backend.domain.admin.entity.Administrator

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
