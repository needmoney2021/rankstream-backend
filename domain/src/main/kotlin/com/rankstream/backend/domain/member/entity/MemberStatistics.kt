package com.rankstream.backend.domain.member.entity

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
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDate

@Entity
@Table(
    name = "member_statistics",
    indexes = [
        Index(name = "UIDX_MEMBER_STATISTICS_DATE_COMPANY", columnList = "company_idx"),
    ]
)
class MemberStatistics(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @Column(nullable = false)
    val issuedOn: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(nullable = false)
    val male: Int,

    @Column(nullable = false)
    val female: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val otherStatistics = when (other) {
            is HibernateProxy -> other.hibernateLazyInitializer.identifier
            is MemberStatistics -> other.idx
            else -> return false
        }
        return this.idx == otherStatistics
    }

    override fun hashCode(): Int {
        return idx?.hashCode() ?: 0
    }
}
