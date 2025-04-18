package com.rankstream.backend.domain.grade.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.grade.dto.response.GradeResponse
import com.rankstream.backend.domain.grade.entity.QGrade
import org.springframework.stereotype.Repository

@Repository
class GradeQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {
    private val grade = QGrade.grade

    fun findByCompanyIdx(companyIdx: Long): List<GradeResponse> =
        jpaQueryFactory
            .select(
                Projections.constructor(
                    GradeResponse::class.java,
                    grade.idx,
                    grade.gradeName,
                    grade.requiredPoint,
                    grade.paybackRatio,
                    grade.createdAt,
                    grade.updatedAt,
                    grade.createdBy,
                    grade.updatedBy
                )
            )
            .from(grade)
            .join(grade.company())
            .where(grade.companyIdxEquals(companyIdx))
            .fetch()

    fun findByCompanyIdxAndIdx(companyIdx: Long, idx: Long): GradeResponse? =
        jpaQueryFactory
            .select(
                Projections.constructor(
                    GradeResponse::class.java,
                    grade.idx,
                    grade.gradeName,
                    grade.requiredPoint,
                    grade.paybackRatio,
                    grade.createdAt,
                    grade.updatedAt,
                    grade.createdBy,
                    grade.updatedBy
                )
            )
            .from(grade)
            .join(grade.company())
            .where(grade.idxEquals(idx), grade.companyIdxEquals(companyIdx))
            .fetchOne()
}

fun QGrade.companyIdxEquals(companyIdx: Long?): BooleanExpression? = companyIdx?.let { this.company().idx.eq(it) }
fun QGrade.idxEquals(idx: Long?): BooleanExpression? = idx?.let { this.idx.eq(it) }
