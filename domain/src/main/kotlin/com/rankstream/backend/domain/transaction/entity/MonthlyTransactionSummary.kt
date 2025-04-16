package com.rankstream.backend.domain.transaction.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.member.entity.Member
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "monthly_transaction_summary",
    indexes = [
        Index(name = "IDX_MONTHLY_TRANSACTION_MEMBER", columnList = "member_idx"),
        Index(name = "IDX_MONTHLY_TRANSACTION_YEAR_MONTH", columnList = "year, month")
    ]
)
class MonthlyTransactionSummary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @Column(nullable = false)
    val year: Int,

    @Column(nullable = false)
    val month: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    val member: Member,

    @Column(nullable = false)
    val totalAmount: Double,

    @Column(nullable = false)
    val totalValueAddedTax: Double,

    @Column(nullable = false)
    val totalGradePoint: Double,

    @Column(nullable = false)
    val totalBusinessPoint: Double
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherSummary = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? MonthlyTransactionSummary)
            is MonthlyTransactionSummary -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherSummary.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "MonthlyTransactionSummary(idx=$idx, year=$year, month=$month, totalAmount=$totalAmount)"
}
