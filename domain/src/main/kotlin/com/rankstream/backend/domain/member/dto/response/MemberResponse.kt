package com.rankstream.backend.domain.member.dto.response

import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.member.enums.MemberPosition
import java.time.LocalDate
import java.time.LocalDateTime

data class MemberGradeHistoryResponse(
    val memberIdx: Long,
    val previousGradeIdx: Long,
    val previousGradeName: String,
    val changedGradeIdx: Long,
    val changedGradeName: String,
    val issuedAt: LocalDateTime
)


data class MemberResponse(
    val companyIdx: Long,
    val companyName: String,
    val idx: Long,
    val id: String,
    val name: String,
    val gender: Gender,
    val sponsorIdx: Long?,
    val sponsorId: String?,
    val sponsorName: String?,
    val recommenderIdx: Long?,
    val recommenderId: String?,
    val recommenderName: String?,
    val state: State,
    val gradeIdx: Long,
    val gradeName: String,
    val position: MemberPosition?,
    val gradeHistory: List<MemberGradeHistoryResponse>,
    val childrenCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String
)

data class MemberStatisticsResponse(
    val issuedOn: LocalDate,
    val male: Int,
    val female: Int,
    val total: Int
)

data class RecommenderSponsorValidationResponse(
    val valid: Boolean,
    val reason: String?
)
