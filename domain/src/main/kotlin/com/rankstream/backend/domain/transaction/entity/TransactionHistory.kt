package com.rankstream.backend.domain.transaction.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.transaction.enums.TxState
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class TransactionHistory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_idx", nullable = false)
    val transaction: Transaction,

    @Column(name = "state", nullable = false, length = 9)
    val state: TxState,

    @Column(name = "amount", nullable = false)
    var amount: Double,

    @Column(name = "rank_point", nullable = false)
    var rankPoint: Double,

    @Column(name = "benefit_point", nullable = false)
    var benefitPoint: Double,

    @Column(name = "date_of_issue", nullable = false)
    val dateOfIssue: LocalDateTime,

) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransactionHistory) return false

        if (idx != other.idx) return false

        return true
    }

    override fun hashCode(): Int {
        return idx?.hashCode() ?: 0
    }

    companion object {
        fun create(transaction: Transaction): TransactionHistory {
            return TransactionHistory(
                idx = null,
                transaction = transaction,
                state = transaction.state,
                amount = transaction.amount,
                rankPoint = transaction.rankPoint,
                benefitPoint = transaction.benefitPoint,
                dateOfIssue = transaction.dateOfIssue
            )
        }
    }
}
