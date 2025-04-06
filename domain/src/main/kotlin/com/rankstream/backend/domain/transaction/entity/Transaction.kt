package com.rankstream.backend.domain.transaction.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.transaction.enums.TxState
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "transaction")
class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    val company: Company,

    @Column(name = "transaction_id", nullable = false, length = 32)
    val transactionId: String,

    @Column(name = "state", nullable = false, length = 9)
    @Enumerated(EnumType.STRING)
    val state: TxState = TxState.COMPLETED,

    @Column(name = "amount", nullable = false)
    var amount: Double,

    @Column(name = "rank_point", nullable = false)
    var rankPoint: Double,

    @Column(name = "benefit_point", nullable = false)
    var benefitPoint: Double,

    @Column(name = "date_of_issue", nullable = false)
    val dateOfIssue: LocalDateTime,

    @OneToMany(mappedBy = "transaction")
    val histories: List<TransactionHistory> = mutableListOf(),

) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Transaction) return false

        if (idx != other.idx) return false

        return true
    }

    override fun hashCode(): Int {
        return idx?.hashCode() ?: 0
    }
}
