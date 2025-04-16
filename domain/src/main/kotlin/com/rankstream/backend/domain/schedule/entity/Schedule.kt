package com.rankstream.backend.domain.schedule.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.State
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "schedule",
    indexes = [
        Index(name = "IDX_SCHEDULE_COMPANY_STATE", columnList = "company_idx, state")
    ]
)
class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(length = 15, nullable = false)
    var cronExpression: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherSchedule = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Schedule)
            is Schedule -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherSchedule.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "Schedule(idx=$idx, cronExpression='$cronExpression')"
}
