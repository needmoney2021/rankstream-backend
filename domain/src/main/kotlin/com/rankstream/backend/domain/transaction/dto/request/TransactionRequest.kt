package com.rankstream.backend.domain.transaction.dto.request

import jakarta.validation.constraints.Positive
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class TransactionSearchRequest(
    val memberId: String? = null,
    val transactionId: String? = null,
    @field:NotNull("OrderedFrom must not be null")
    val orderedFrom: LocalDateTime,
    @field:NotNull("OrderedTo must not be null")
    val orderedTo: LocalDateTime
)

data class ClosedTransactionSearchRequest(
    @field:NotNull("Start Year must not be null")
    @field:Positive("Start Year must be positive")
    val startYear: Int,
    @field:NotNull("Start Month must not be null")
    @field:Positive("Start Month must be positive")
    val startMonth: Int,
    @field:NotNull("End Year must not be null")
    @field:Positive("End Year must be positive")
    val endYear: Int,
    @field:NotNull("End Month must not be null")
    @field:Positive("End Month must be positive")
    val endMonth: Int,

    val memberId: String? = null
)
