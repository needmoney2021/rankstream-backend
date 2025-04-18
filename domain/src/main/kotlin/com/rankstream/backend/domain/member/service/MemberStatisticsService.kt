package com.rankstream.backend.domain.member.service

import com.rankstream.backend.domain.member.dto.response.MemberStatisticsResponse
import com.rankstream.backend.domain.member.repository.MemberStatisticsQueryDslRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberStatisticsService(
    private val memberStatisticsQueryDslRepository: MemberStatisticsQueryDslRepository
) {

    fun findLastThirtyDaysStatisticsByCompanyIdx(companyIdx: Long): List<MemberStatisticsResponse> {
        return memberStatisticsQueryDslRepository.findLastThirtyDaysByCompanyIdx(companyIdx)
    }
}
