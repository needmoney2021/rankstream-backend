package com.rankstream.backend.auth.service

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.repository.AdministratorQueryDslRepository
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.grade.entity.Grade
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class JwtServiceTest : StringSpec() {

    private val tokenKeyForTest = "Test token key"
    private val issuerForTest = "RankStream Backend"
    private val passwordEncoder = mockk<BCryptPasswordEncoder>()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtServiceTest::class.java)
    }

    init {

        val repos = mockk<AdministratorQueryDslRepository>("MockAdminRepository")

        val jwtService = JwtService(repos, tokenKeyForTest, issuerForTest, passwordEncoder)
        val mockCompany = mockk<Company>()
        val mockGrade = mockk<Grade>()
        val plainPassword = "testpassword"
        val hashedPassword = "\$2a\$10\$dummyhashedvalue1234567890abc"
        val userId = "needmoney@gmail.com"

        "should generate valid access and refresh tokens" {

            val mockAdmin = Administrator(
                idx = 1L,
                company = mockCompany,
                userId = userId,
                password = hashedPassword,
                userName = "니드머니",
                state = State.ACTIVE,
                department = "Test Department"
            )

            every { repos.findByUserId("needmoney@gmail.com") } returns mockAdmin
            every { passwordEncoder.matches(plainPassword, hashedPassword) } returns true

            val accessToken = jwtService.generateAccessToken(mockAdmin, plainPassword)
            val refreshToken = jwtService.generateRefreshToken(1L)

            log.info("Access token: $accessToken")
            log.info("Refresh token: $refreshToken")

            val decodedToken = jwtService.decodeToken(accessToken)
            decodedToken.subject shouldNotBe null
            decodedToken.subject shouldBe "1"
            decodedToken.issuer shouldBe issuerForTest
        }


    }
}
