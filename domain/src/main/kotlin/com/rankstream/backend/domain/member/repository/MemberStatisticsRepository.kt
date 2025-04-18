package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.entity.MemberStatistics
import org.springframework.data.jpa.repository.JpaRepository

interface MemberStatisticsRepository : JpaRepository<MemberStatistics, Long> {
}
