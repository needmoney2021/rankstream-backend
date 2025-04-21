package com.rankstream.backend.domain.member.dto.tree

data class MemberTreeDto(
    val idx: Long,
    val memberId: String,
    val memberName: String,
    val position: String?,
    val gradeIdx: Long,
    val depth: Int
)
