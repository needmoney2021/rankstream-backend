package com.rankstream.backend.auth.service

import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.grade.entity.Grade
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.repository.MemberQueryDslRepository
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


    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtServiceTest::class.java)
    }

    init {

        val repos = mockk<MemberQueryDslRepository>("MockMemberRepository")
        val encoder = mockk<BCryptPasswordEncoder>("MockPasswordEncoder")

        val jwtService = JwtService(repos, encoder, tokenKeyForTest, issuerForTest)
        val mockCompany = mockk<Company>()
        val mockGrade = mockk<Grade>()
        val plainPassword = "testpassword"
        val hashedPassword = "\$2a\$10\$dummyhashedvalue1234567890abc"
        val memberId = "needmoney@gmail.com"

        "should generate valid access and refresh tokens" {

            every { repos.findMemberByMemberId("needmoney@gmail.com") } returns Member(
                idx = 1L,
                memberId = memberId,
                password = hashedPassword,
                company = mockCompany,
                memberName = "니드머니",
                gender = Gender.MALE,
                grade = mockGrade,
                state = State.ACTIVE
            )

            every { encoder.matches(plainPassword, hashedPassword) } returns true

            val accessToken = jwtService.generateAccessToken(memberId, plainPassword)
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
