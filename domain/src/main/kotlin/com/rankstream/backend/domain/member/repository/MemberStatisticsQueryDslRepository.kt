package com.rankstream.backend.domain.member.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.member.dto.response.MemberStatisticsResponse
import com.rankstream.backend.domain.member.entity.QMemberStatistics
import org.springframework.stereotype.Repository

@Repository
class MemberStatisticsQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val memberStatistics = QMemberStatistics.memberStatistics

    fun findLastThirtyDaysByCompanyIdx(companyIdx: Long): List<MemberStatisticsResponse> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    MemberStatisticsResponse::class.java,
                    memberStatistics.issuedOn,
                    memberStatistics.male,
                    memberStatistics.female,
                    memberStatistics.male.add(memberStatistics.female)
                )
            )
            .from(memberStatistics)
            .where(memberStatistics.companyIdxEquals(companyIdx))
            .orderBy(memberStatistics.issuedOn.asc())
            .limit(30)
            .fetch()
    }
}

fun QMemberStatistics.companyIdxEquals(companyIdx: Long?): BooleanExpression? = companyIdx?.let { this.company().idx.eq(it) }
