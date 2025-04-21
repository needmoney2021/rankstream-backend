package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.entity.MemberGradeHistory
import org.springframework.data.jpa.repository.JpaRepository

interface MemberGradeHistoryRepository : JpaRepository<MemberGradeHistory, Long> {
}
