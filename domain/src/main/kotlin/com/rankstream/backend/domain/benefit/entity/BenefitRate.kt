package com.rankstream.backend.domain.benefit.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "benefit_rate",
    indexes = [
        Index(name = "IDX_MINIMUM_POINT", columnList = "min_points")
    ]
)
class BenefitRate(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(name = "rate", nullable = false)
    var rate: Double = 0.00,

    @Column(name = "min_points", nullable = false)
    var minimumPoints: Int = 0
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BenefitRate) return false

        if (idx != other.idx) return false

        return true
    }

    override fun hashCode(): Int {
        return idx?.hashCode() ?: 0
    }

}
