package com.rankstream.backend.domain.member.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.member.dto.request.MemberSearchRequest
import com.rankstream.backend.domain.member.dto.response.MemberGradeHistoryResponse
import com.rankstream.backend.domain.member.dto.response.MemberResponse
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.entity.QMember
import com.rankstream.backend.domain.member.entity.QMemberClosure
import com.rankstream.backend.domain.member.entity.QMemberGradeHistory
import org.springframework.stereotype.Repository

@Repository
class MemberQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val member = QMember.member
    private val sponsor = QMember("sponsor")
    private val recommender = QMember("recommender")
    private val memberClosure = QMemberClosure.memberClosure
    private val memberGradeHistory = QMemberGradeHistory.memberGradeHistory

    // ----------------------------
    // [1] 리스트 조회 (gradeHistory는 빈 리스트)
    // ----------------------------
    fun findByCompanyIdx(companyIdx: Long, memberSearchRequest: MemberSearchRequest): List<MemberResponse> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    MemberResponse::class.java,
                    member.company().idx,
                    member.company().companyName,
                    member.idx,
                    member.memberId,
                    member.memberName,
                    member.gender,
                    sponsor.idx,
                    sponsor.memberId,
                    sponsor.memberName,
                    recommender.idx,
                    recommender.memberId,
                    recommender.memberName,
                    member.state,
                    member.grade().idx,
                    member.grade().gradeName,
                    member.position,
                    Expressions.constant(emptyList<MemberGradeHistoryResponse>()), // ← 빈 리스트
                    JPAExpressions.select(memberClosure.count())
                        .from(memberClosure)
                        .where(
                            memberClosure.ancestor().idx.eq(member.idx)
                                .and(memberClosure.depth.eq(1))
                        ),
                    member.createdAt,
                    member.updatedAt,
                    member.createdBy,
                    member.updatedBy
                )
            )
            .from(member)
            .leftJoin(member.grade())
            .leftJoin(member.recommender(), recommender)
            .leftJoin(member.company())
            .leftJoin(member.sponsor(), sponsor)
            .where(
                member.companyIdxEquals(companyIdx),
                member.memberIdEquals(memberSearchRequest.id),
                member.memberNameContains(memberSearchRequest.name),
                member.genderEquals(memberSearchRequest.gender)
            )
            .orderBy(member.createdAt.desc())
            .fetch()
    }


    // ----------------------------
    // [2] 상세 조회 (gradeHistory 포함)
    // ----------------------------
    fun findDetailByMemberIdxAndCompanyIdx(memberIdx: Long, companyIdx: Long): MemberResponse? {
        val result = jpaQueryFactory
            .select(
                Projections.constructor(
                    MemberResponse::class.java,
                    member.company().idx,
                    member.company().companyName,
                    member.idx,
                    member.memberId,
                    member.memberName,
                    member.gender,
                    sponsor.idx,
                    sponsor.memberId,
                    sponsor.memberName,
                    recommender.idx,
                    recommender.memberId,
                    recommender.memberName,
                    member.state,
                    member.grade().idx,
                    member.grade().gradeName,
                    member.position,
                    Expressions.constant(emptyList<MemberGradeHistoryResponse>()),
                    JPAExpressions.select(memberClosure.count())
                        .from(memberClosure)
                        .where(
                            memberClosure.ancestor().idx.eq(member.idx)
                                .and(memberClosure.depth.eq(1))
                        ),
                    member.createdAt,
                    member.updatedAt,
                    member.createdBy,
                    member.updatedBy
                )
            )
            .from(member)
            .leftJoin(member.grade())
            .leftJoin(member.recommender(),recommender)
            .leftJoin(member.company())
            .leftJoin(member.sponsor(), sponsor)
            .where(member.idx.eq(memberIdx))
            .fetchOne()

        if (result == null) return null

        val gradeHistory = jpaQueryFactory
            .select(
                Projections.constructor(
                    MemberGradeHistoryResponse::class.java,
                    memberGradeHistory.member().idx,
                    memberGradeHistory.previous().idx,
                    memberGradeHistory.previous().gradeName,
                    memberGradeHistory.changed().idx,
                    memberGradeHistory.changed().gradeName,
                    memberGradeHistory.issuedAt
                )
            )
            .from(memberGradeHistory)
            .leftJoin(memberGradeHistory.previous())
            .leftJoin(memberGradeHistory.changed())
            .where(
                memberGradeHistory.member().memberIdxEquals(memberIdx),
                memberGradeHistory.member().companyIdxEquals(companyIdx)
            )
            .orderBy(memberGradeHistory.issuedAt.desc())
            .fetch()

        return result.copy(gradeHistory = gradeHistory)
    }

}



fun QMember.companyIdxEquals(companyIdx: Long?): BooleanExpression? {
    return companyIdx?.let {
        this.company().idx.eq(it)
    }
}

fun QMember.memberIdEquals(memberId: String?): BooleanExpression? {
    return memberId?.let {
        this.memberId.eq(it)
    }
}

fun QMember.memberNameContains(memberName: String?): BooleanExpression? {
    return memberName?.let {
        this.memberName.containsIgnoreCase(it)
    }
}

fun QMember.genderEquals(gender: Gender?): BooleanExpression? {
    return gender?.let {
        this.gender.eq(it)
    }
}

fun QMember.memberIdxEquals(memberIdx: Long?): BooleanExpression? {
    return memberIdx?.let {
        this.idx.eq(memberIdx)
    }
}
