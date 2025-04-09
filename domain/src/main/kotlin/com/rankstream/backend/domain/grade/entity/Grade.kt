package com.rankstream.backend.domain.grade.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import jakarta.persistence.*
import jakarta.persistence.Index
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "grade",
    indexes = [
        Index(name = "IDX-GRADE-COMPANY-CODE", columnList = "company_idx, grade_code")
    ]
)
class Grade(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(length = 10, nullable = false)
    val gradeCode: String,

    @Column(length = 20, nullable = false)
    val gradeName: String,

    @Column(nullable = false)
    var requiredPoint: Int,

    @Column(nullable = false)
    var paybackRatio: Double
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        
        val otherGrade = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Grade)
            is Grade -> other
            else -> return false
        } ?: return false
        
        return idx != null && idx == otherGrade.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "Grade(idx=$idx, gradeName='$gradeName')"
} 