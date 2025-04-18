package com.rankstream.backend.domain.member.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "member_closure",
    indexes = [
        Index(name = "IDX_MEMBER_CLOSURE_ANC_DESC", columnList = "ancestor_idx, descendant_idx"),
        Index(name = "IDX_MEMBER_CLOSURE_ANCESTOR", columnList = "ancestor_idx"),
        Index(name = "IDX_MEMBER_CLOSURE_DESCENDANT", columnList = "descendant_idx"),
        Index(name = "IDX_MEMBER_CLOSURE_DEPTH", columnList = "depth")
    ]
)
class MemberClosure(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestor_idx", nullable = false, foreignKey = ForeignKey(name = "FK_MEMBER_CLOSURE_ANCESTOR"))
    val ancestor: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "descendant_idx", nullable = false, foreignKey = ForeignKey(name = "FK_MEMBER_CLOSURE_DESCENDANT"))
    val descendant: Member,

    @Column(nullable = false)
    val depth: Int
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherMemberClosure = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? MemberClosure)
            is MemberClosure -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherMemberClosure.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "MemberClosure(idx=$idx, ancestor=${ancestor.idx}, descendant=${descendant.idx}, depth=$depth)"
}
