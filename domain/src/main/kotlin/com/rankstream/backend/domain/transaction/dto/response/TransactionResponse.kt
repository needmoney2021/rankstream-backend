package com.rankstream.backend.domain.transaction.dto.response

import java.time.LocalDateTime

data class TransactionResponse(
    val idx: Long,
    val memberIdx: Long,
    val memberName: String,
    val transactionId: String,
    val amount: Double,
    val gradePoint: Double,
    val businessPoint: Double,
    val valueAddedTax: Double,
    val orderedAt: LocalDateTime,
    val closed: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class ClosedTransactionResponse(
    val year: Int,
    val month: Int,
    val totalAmount: Double,
    val totalValueAddedTax: Double,
    val totalGradePoint: Double,
    val totalBusinessPoint: Double,
    val memberIdx: Long,
    val memberName: String
)
