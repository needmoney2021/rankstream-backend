package com.rankstream.backend.domain.transaction.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.transaction.dto.request.TransactionSearchRequest
import com.rankstream.backend.domain.transaction.dto.response.TransactionResponse
import com.rankstream.backend.domain.transaction.entity.QTransaction
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TransactionQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val tx = QTransaction.transaction

    fun findTransactionsByCompanyIdxAndConditions(
        companyIdx: Long,
        transactionSearchRequest: TransactionSearchRequest
    ): List<TransactionResponse> {
        return jpaQueryFactory.select(
            Projections.constructor(
                TransactionResponse::class.java,
                tx.idx,
                tx.member().idx,
                tx.member().memberName,
                tx.transactionId,
                tx.amount,
                tx.gradePoint,
                tx.businessPoint,
                tx.valueAddedTax,
                tx.orderedAt,
                tx.closed,
                tx.createdAt,
                tx.updatedAt
            )
        )
            .from(tx)
            .where(
                tx.companyIdxEquals(companyIdx),
                tx.memberIdEquals(transactionSearchRequest.memberId),
                tx.transactionIdEquals(transactionSearchRequest.transactionId),
                tx.orderedFrom(transactionSearchRequest.orderedFrom),
                tx.orderedTo(transactionSearchRequest.orderedTo)
            )
            .orderBy(
                tx.orderedAt.desc()
            )
            .fetch()
    }
}

fun QTransaction.memberIdEquals(memberId: String?) = memberId?.let { this.member().memberId.eq(memberId) }
fun QTransaction.companyIdxEquals(companyIdx: Long?) = companyIdx?.let { this.member().company().idx.eq(companyIdx) }
fun QTransaction.transactionIdEquals(transactionId: String?) =
    transactionId?.let { this.transactionId.eq(transactionId) }

fun QTransaction.orderedFrom(from: LocalDateTime?) = from?.let { this.createdAt.goe(from) }
fun QTransaction.orderedTo(to: LocalDateTime?) = to?.let { this.createdAt.loe(to) }
