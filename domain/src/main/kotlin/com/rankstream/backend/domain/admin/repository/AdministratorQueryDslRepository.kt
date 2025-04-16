package com.rankstream.backend.domain.admin.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.entity.QAdministrator
import org.springframework.stereotype.Repository

@Repository
class AdministratorQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val administrator = QAdministrator.administrator

    fun findByUserId(userId: String): Administrator? {
        return jpaQueryFactory.selectFrom(administrator)
            .join(administrator.company()).fetchJoin()
            .where(administrator.userIdEquals(userId))
            .fetchOne()
    }

    fun findByIdx(idx: Long): Administrator? {
        return jpaQueryFactory.selectFrom(administrator)
            .join(administrator.company()).fetchJoin()
            .where(administrator.idxEquals(idx))
            .fetchOne()
    }
}

fun QAdministrator.userIdEquals(userId: String?): BooleanExpression? = userId?.let { this.userId.eq(it) }
fun QAdministrator.idxEquals(idx: Long?): BooleanExpression? = idx?.let { this.idx.eq(it) }
