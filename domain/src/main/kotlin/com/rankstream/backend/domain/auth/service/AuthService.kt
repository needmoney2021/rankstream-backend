package com.rankstream.backend.domain.auth.service

import com.rankstream.backend.domain.auth.entity.RefreshToken
import com.rankstream.backend.domain.auth.repository.RefreshTokenQueryDslRepository
import com.rankstream.backend.domain.auth.repository.RefreshTokenRepository
import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val refreshTokenQueryDslRepository: RefreshTokenQueryDslRepository
) {

    @Transactional(readOnly = false)
    fun replaceRefreshToken(oldToken: String, newToken: String) {
        refreshTokenRepository.deleteByRefreshToken(oldToken)
        refreshTokenRepository.save(
            RefreshToken(refreshToken = newToken)
        )
    }

    @Transactional(readOnly = false)
    fun saveRefreshToken(refreshToken: String) {
        refreshTokenRepository.save(
            RefreshToken(refreshToken = refreshToken)
        )
    }

    @Transactional(readOnly = false)
    fun deleteRefreshToken(refreshToken: String) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken)
    }

    fun findRefreshTokenInWhiteList(refreshToken: String): RefreshToken {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
            ?: throw UnauthorizedException("Invalid refresh token.", ErrorCode.AUTHENTICATION_FAILED)
    }

}
