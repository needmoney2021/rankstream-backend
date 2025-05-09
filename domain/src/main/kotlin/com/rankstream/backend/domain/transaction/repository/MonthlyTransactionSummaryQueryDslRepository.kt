package com.rankstream.backend.domain.transaction.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.transaction.dto.request.ClosedTransactionSearchRequest
import com.rankstream.backend.domain.transaction.dto.response.ClosedTransactionResponse
import com.rankstream.backend.domain.transaction.entity.QMonthlyTransactionSummary
import org.springframework.stereotype.Repository

@Repository
class MonthlyTransactionSummaryQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    private val txSummary = QMonthlyTransactionSummary.monthlyTransactionSummary

    fun findClosedTransactionByCompanyIdxAndConditions(companyIdx: Long, closedTransactionSearchRequest: ClosedTransactionSearchRequest): List<ClosedTransactionResponse> {
        return jpaQueryFactory.select(
            Projections.constructor(
                ClosedTransactionResponse::class.java,
                txSummary.year,
                txSummary.month,
                txSummary.totalAmount,
                txSummary.totalValueAddedTax,
                txSummary.totalGradePoint,
                txSummary.totalBusinessPoint,
                txSummary.member().idx,
                txSummary.member().memberName
            )
        )
            .from(txSummary)
            .where(
                txSummary.companyIdxEquals(companyIdx),
                txSummary.memberIdEquals(closedTransactionSearchRequest.memberId),
                txSummary.yearFrom(closedTransactionSearchRequest.startYear),
                txSummary.yearTo(closedTransactionSearchRequest.endYear),
                txSummary.monthFrom(closedTransactionSearchRequest.startMonth),
                txSummary.monthTo(closedTransactionSearchRequest.endMonth)
            )
            .orderBy(
                txSummary.year.desc(),
                txSummary.month.asc(),
                txSummary.member().idx.asc()
            )
            .fetch()
    }
}

fun QMonthlyTransactionSummary.memberIdEquals(memberId: String?) = memberId?.let { this.member().memberId.eq(memberId) }
fun QMonthlyTransactionSummary.companyIdxEquals(companyIdx: Long?) = companyIdx?.let { this.member().company().idx.eq(companyIdx) }
fun QMonthlyTransactionSummary.yearFrom(from: Int?) = from?.let { this.year.goe(from) }
fun QMonthlyTransactionSummary.yearTo(to: Int?) = to?.let { this.year.loe(to) }
fun QMonthlyTransactionSummary.monthFrom(from: Int?) = from?.let { this.month.goe(from) }
fun QMonthlyTransactionSummary.monthTo(to: Int?) = to?.let { this.month.loe(to) }
