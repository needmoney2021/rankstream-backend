package com.rankstream.backend.domain.member.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.entity.QMember
import org.springframework.stereotype.Repository

@Repository
class MemberQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val member: QMember = QMember.member

    fun findMemberByMemberId(memberId: String): Member? {
        return jpaQueryFactory.selectFrom(member)
            .where(member.memberIdEquals(memberId))
            .fetchOne()
    }

    fun findMemberByIdx(memberIdx: Long): Member? {
        return jpaQueryFactory.selectFrom(member)
            .where(member.memberIdxEquals(memberIdx))
            .fetchOne()
    }

}

fun QMember.memberIdEquals(memberId: String?): BooleanExpression? {
    return memberId?.let {
        this.memberId.equalsIgnoreCase(it)
    }
}

fun QMember.memberIdxEquals(memberIdx: Long?): BooleanExpression? {
    return memberIdx?.let {
        this.idx.eq(memberIdx)
    }
}
