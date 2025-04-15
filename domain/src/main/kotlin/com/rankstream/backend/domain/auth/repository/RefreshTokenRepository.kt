package com.rankstream.backend.domain.auth.repository

import com.rankstream.backend.domain.auth.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {

    fun deleteByRefreshToken(refreshToken: String)

    fun findByRefreshToken(refreshToken: String): RefreshToken?
}
