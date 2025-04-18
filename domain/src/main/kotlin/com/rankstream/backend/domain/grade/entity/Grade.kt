package com.rankstream.backend.domain.grade.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.grade.dto.request.GradeRegistrationRequest
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "grade",
    indexes = [
        Index(name = "IDX_GRADE_COMPANY", columnList = "company_idx")
    ]
)
class Grade(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(length = 20, nullable = false)
    var gradeName: String,

    @Column(nullable = false)
    var requiredPoint: Double,

    @Column(nullable = false)
    var paybackRatio: Double
) : TimestampEntityListener() {

    companion object {
        fun create(request: GradeRegistrationRequest, company: Company): Grade {
            return Grade(
                company = company,
                gradeName = request.name,
                requiredPoint = request.achievementPoint,
                paybackRatio = request.refundRate
            )
        }
    }

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
