package com.rankstream.backend.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.enums.MemberType
import com.rankstream.backend.domain.member.repository.MemberQueryDslRepository
import com.rankstream.backend.exception.ForbiddenException
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.UnauthorizedException
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class JwtService(
    private val memberQueryDslRepository: MemberQueryDslRepository,
    private val passwordEncoder: PasswordEncoder,

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.issuer}")
    private val issuer: String,
) {

    companion object {
        private const val ACCESS_TOKEN_EXPIRE_SECONDS = 1800L // 30분
        private const val REFRESH_TOKEN_EXPIRE_SECONDS = 60L * 60 * 24 * 30 // 30일
    }

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(memberId: String, password: String): String {
        val member = findMemberOrThrow(memberId)
        if (!passwordEncoder.matches(password, member.password)) {
            throw UnauthorizedException("Password does not match.", ErrorCode.WRONG_PASSWORD)
        }

        return generateToken(member.idx!!, ACCESS_TOKEN_EXPIRE_SECONDS)
    }

    fun generateRefreshToken(memberIdx: Long): String {
        return generateToken(memberIdx, REFRESH_TOKEN_EXPIRE_SECONDS)
    }

    fun generateNewTokensViaRefreshToken(refreshToken: String): List<String> {
        val decoded = decodeToken(refreshToken)
        val memberIdx = decoded.subject
        val member = findMemberOrThrow(memberIdx.toLong())

        return listOf(generateToken(member.idx!!, ACCESS_TOKEN_EXPIRE_SECONDS), generateToken(member.idx!!, REFRESH_TOKEN_EXPIRE_SECONDS))
    }

    fun decodeToken(token: String): DecodedJWT {
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .acceptLeeway(60)
            .build()

        try {
            return verifier.verify(token)
        } catch (e: TokenExpiredException) {
            throw UnauthorizedException("Token expired.", ErrorCode.TOKEN_EXPIRED)
        } catch (e: JWTVerificationException) {
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

    private fun findMemberOrThrow(memberId: String): Member {
        val member = memberQueryDslRepository.findMemberByMemberId(memberId)
            ?: throw NotFoundException("Member with id: $memberId does not exist", ErrorCode.MEMBER_NOT_FOUND, memberId)
        if (!isAdministrator(member)) {
            throw ForbiddenException("Access denied.", ErrorCode.FORBIDDEN)
        }
        return member
    }

    private fun findMemberOrThrow(memberIdx: Long): Member {
        val member = memberQueryDslRepository.findMemberByIdx(memberIdx)
            ?: throw throw NotFoundException("Member with id: $memberIdx does not exist", ErrorCode.MEMBER_NOT_FOUND, memberIdx)
        if (!isAdministrator(member)) {
            throw ForbiddenException("Access denied.", ErrorCode.FORBIDDEN)
        }
        return member
    }

    private fun isAdministrator(member: Member): Boolean {
        return member.type == MemberType.ADMIN
    }


}
