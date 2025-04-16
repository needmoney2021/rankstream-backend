package com.rankstream.backend.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.repository.AdministratorQueryDslRepository
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.exception.ForbiddenException
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.enums.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class JwtService(
    private val administratorQueryDslRepository: AdministratorQueryDslRepository,

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.issuer}")
    private val issuer: String,

    private val passwordEncoder: PasswordEncoder
) {

    companion object {
        private val log = LoggerFactory.getLogger(JwtService::class.java)
        const val ACCESS_TOKEN_EXPIRE_SECONDS = 1800L // 30분
        const val REFRESH_TOKEN_EXPIRE_SECONDS = 60L * 60 * 24 * 30 // 30일
    }

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(administrator: Administrator, password: String): String {
        if (!passwordEncoder.matches(password, administrator.password)) {
            throw UnauthorizedException("Password does not match.", ErrorCode.WRONG_PASSWORD)
        }

        return generateToken(administrator.idx!!, ACCESS_TOKEN_EXPIRE_SECONDS)
    }

    fun generateRefreshToken(administratorIdx: Long): String {
        return generateToken(administratorIdx, REFRESH_TOKEN_EXPIRE_SECONDS)
    }

    fun generateNewTokensViaRefreshToken(refreshToken: String): TokenPair {
        val decoded = decodeToken(refreshToken)
        val memberIdx = decoded.subject
        val member = findAdministratorOrThrow(memberIdx.toLong())

        return TokenPair(generateToken(member.idx!!, ACCESS_TOKEN_EXPIRE_SECONDS), generateToken(member.idx!!, REFRESH_TOKEN_EXPIRE_SECONDS))
    }

    fun decodeToken(token: String): DecodedJWT {
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .acceptLeeway(60)
            .build()

        try {
            return verifier.verify(token)
        } catch (e: TokenExpiredException) {
            log.error(e.stackTraceToString())
            throw UnauthorizedException("Token expired.", ErrorCode.TOKEN_EXPIRED)
        } catch (e: JWTVerificationException) {
            log.error(e.stackTraceToString())
            throw UnauthorizedException("Token verification failed.", ErrorCode.AUTHENTICATION_FAILED)
        }
    }

    private fun generateToken(memberIdx: Long, expiration: Long): String {
        val now = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"))
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(memberIdx.toString())
            .withIssuedAt(now.toInstant())
            .withExpiresAt(now.plusSeconds(expiration).toInstant())
            .sign(algorithm)
    }

    private fun findAdministratorOrThrow(memberIdx: Long): Administrator {
        val administrator = administratorQueryDslRepository.findByIdx(memberIdx)
            ?: throw throw NotFoundException("Member with id: $memberIdx does not exist", ErrorCode.MEMBER_NOT_FOUND, memberIdx)
        if (!isActive(administrator)) {
            throw ForbiddenException("Access denied.", ErrorCode.FORBIDDEN)
        }
        return administrator
    }

    private fun isActive(administrator: Administrator): Boolean {
        return administrator.state == State.ACTIVE
    }

}

data class TokenPair(val accessToken: String, val refreshToken: String)
