package com.rankstream.backend.domain.member.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.grade.entity.Grade
import com.rankstream.backend.domain.member.dto.request.MemberRegistrationRequest
import com.rankstream.backend.domain.member.enums.MemberPosition
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
        Index(name = "IDX_MEMBER_COMPANY_RECOMMENDER", columnList = "company_idx, recommender_idx"),
        Index(name = "IDX_MEMBER_COMPANY_SPONSOR", columnList = "company_idx, sponsor_idx"),
        Index(name = "IDX_MEMBER_COMPANY_SPONSOR_POSITION", columnList = "company_idx, sponsor_idx, position"),
        Index(name = "IDX_MEMBER_COMPANY_GRADE", columnList = "company_idx, grade_idx"),
        Index(name = "IDX_MEMBER_COMPANY_STATE", columnList = "company_idx, state"),


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

    @Column(length = 50, nullable = false)
    val memberName: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    val gender: Gender,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommender_idx", nullable = true)
    val recommender: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_idx", nullable = true)
    val sponsor: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_idx")
    var grade: Grade,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State,

    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = true)
    val position: MemberPosition? = null,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("issuedAt DESC")
    val gradeHistory: MutableList<MemberGradeHistory> = mutableListOf()

) : TimestampEntityListener() {

    companion object {
        fun create(company: Company, grade: Grade, recommender: Member?, sponsor: Member?, memberRegistrationRequest: MemberRegistrationRequest): Member {
            return Member(
                company = company,
                memberId = memberRegistrationRequest.id,
                memberName = memberRegistrationRequest.name,
                gender = memberRegistrationRequest.gender,
                grade = grade,
                sponsor = sponsor,
                recommender = recommender,
                position = memberRegistrationRequest.position,
                state = State.ACTIVE
            )
        }
    }

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

    fun isActive(): Boolean = state == State.ACTIVE
}
