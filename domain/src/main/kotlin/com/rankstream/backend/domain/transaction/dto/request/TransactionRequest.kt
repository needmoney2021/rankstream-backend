package com.rankstream.backend.domain.transaction.dto.request

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
