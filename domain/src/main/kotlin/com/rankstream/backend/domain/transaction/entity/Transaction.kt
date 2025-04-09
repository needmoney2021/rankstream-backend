package com.rankstream.backend.domain.transaction.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.enums.TransactionState
import com.rankstream.backend.domain.member.entity.Member
import jakarta.persistence.*
import jakarta.persistence.Index
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "transaction",
    indexes = [
        Index(name = "IDX-TRANSACTION-MEMBER", columnList = "member_idx"),
        Index(name = "IDX-TRANSACTION-ID", columnList = "transaction_id"),
        Index(name = "IDX-TRANSACTION-STATE", columnList = "state"),
        Index(name = "IDX-TRANSACTION-ORDERED", columnList = "ordered_at"),
        Index(name = "IDX-TRANSACTION-CLOSED", columnList = "closed")
    ]
)
class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    val member: Member,

    @Column(length = 30, nullable = false)
    val transactionId: String,

    @Column(nullable = false)
    val amount: Double,

    @Column(nullable = false)
    val gradePoint: Double,

    @Column(nullable = false)
    val businessPoint: Double,

    @Column(nullable = false)
    val valueAddedTax: Double,

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    var state: TransactionState,

    @Column(nullable = false)
    val orderedAt: LocalDateTime,

    @Column(nullable = false)
    var closed: Boolean
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        
        val otherTransaction = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Transaction)
            is Transaction -> other
            else -> return false
        } ?: return false
        
        return idx != null && idx == otherTransaction.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "Transaction(idx=$idx, transactionId='$transactionId', amount=$amount)"
} 