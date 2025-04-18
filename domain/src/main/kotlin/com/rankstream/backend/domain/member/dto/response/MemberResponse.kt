package com.rankstream.backend.domain.member.dto.response

import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import java.time.LocalDateTime

data class MemberGradeHistoryResponse(
    val gradeIdx: Long,
    val gradeName: String,
    val issuedAt: LocalDateTime
)


data class MemberResponse(
    val companyIdx: Long,
    val companyName: String,
    val idx: Long,
    val id: String,
    val name: String,
    val gender: Gender,
    val recommenderIdx: Long?,
    val recommenderName: String?,
    val sponsorIdx: Long?,
    val sponsorName: String?,
    val state: State,
    val gradeIdx: Long,
    val gradeName: String,
    val gradeHistory: List<MemberGradeHistoryResponse>,
    val childrenCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String
)

data class TreeNode(
    val idx: Long,
    val id: String,
    val name: String,
    val children: List<MemberTreeResponse>?
)

data class TreeLink(
    val source: Long,
    val target: Long
)

data class MemberTreeResponse(
    val nodes: List<TreeNode>,
    val links: List<TreeLink>
)
