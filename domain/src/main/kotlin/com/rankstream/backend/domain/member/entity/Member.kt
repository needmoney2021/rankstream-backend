package com.rankstream.backend.domain.member.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.grade.entity.Grade
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "member",
    indexes = [
        // 기본 검색 필터 최적화 (company_idx는 항상 맨 앞!)
        Index(name = "IDX_MEMBER_COMPANY_ID", columnList = "company_idx, member_id"),
        Index(name = "IDX_MEMBER_COMPANY_NAME", columnList = "company_idx, member_name"),
        Index(name = "IDX_MEMBER_COMPANY_GENDER", columnList = "company_idx, gender"),

        // 정렬 성능 최적화
        Index(name = "IDX_MEMBER_COMPANY_CREATED_AT", columnList = "company_idx, created_at"),

        // 유니크 제약
        Index(name = "UIDX_MEMBER_ID_COMPANY", columnList = "member_id, company_idx", unique = true)
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @Column(length = 50, nullable = false)
    val memberId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(length = 15, nullable = false)
    val memberName: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    val gender: Gender,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_idx")
    var grade: Grade,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("issuedAt DESC")
    val gradeHistory: MutableList<MemberGradeHistory> = mutableListOf()

) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherMember = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Member)
            is Member -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherMember.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "Member(idx=$idx, memberId='$memberId', memberName='$memberName')"
}
