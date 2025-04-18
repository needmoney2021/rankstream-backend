package com.rankstream.backend.domain.admin.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.admin.dto.request.AdministratorSearchRequest
import com.rankstream.backend.domain.admin.dto.response.AdministratorResponse
import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.entity.QAdministrator
import com.rankstream.backend.domain.enums.State
import org.springframework.stereotype.Repository

@Repository
class AdministratorQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val administrator = QAdministrator.administrator

    // Entity 자체를 리턴하는 함수는 JpaRepository 확장 인터페이스에 선언하는 것을 원칙으로 함.
    // 그러나 아래 두 함수는 auth 모듈에서 참조하는데
    // auth 모듈에서는 Jpa에 대한 의존이 없어 JpaRepository 확장 인터페이스 접근이 불가함.
    // 따라서 불가피하게 여기에 작성하였음.
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

    fun findAdministratorsByCondition(administratorSearchRequest: AdministratorSearchRequest): List<AdministratorResponse> {
        return jpaQueryFactory.select(
            Projections.constructor<AdministratorResponse>(
                AdministratorResponse::class.java,
                administrator.idx,
                administrator.company().companyName,
                administrator.userId,
                administrator.userName,
                administrator.department,
                administrator.state,
                administrator.createdAt,
                administrator.updatedAt,
                administrator.createdBy,
                administrator.updatedBy
            )
        ).from(administrator)
            .join(administrator.company())
            .where(
                administrator.companyIdxEquals(administratorSearchRequest.companyIdx),
                administrator.userIdEquals(administratorSearchRequest.id),
                administrator.userNameContains(administratorSearchRequest.name),
                administrator.stateEquals(administratorSearchRequest.state)
            )
            .fetch()
    }

    fun findByCompanyAndIdx(companyIdx: Long, idx: Long): AdministratorResponse? {
        return jpaQueryFactory.select(
            Projections.constructor<AdministratorResponse>(
                AdministratorResponse::class.java,
                administrator.idx,
                administrator.company().companyName,
                administrator.userId,
                administrator.userName,
                administrator.department,
                administrator.state,
                administrator.createdAt,
                administrator.updatedAt,
                administrator.createdBy,
                administrator.updatedBy
            )
        ).from(administrator)
            .join(administrator.company())
            .where(
                administrator.idxEquals(idx),
                administrator.companyIdxEquals(companyIdx)
            )
            .fetchOne()
    }
}

fun QAdministrator.userIdEquals(userId: String?): BooleanExpression? = userId?.let { this.userId.eq(it) }
fun QAdministrator.idxEquals(idx: Long?): BooleanExpression? = idx?.let { this.idx.eq(it) }
fun QAdministrator.companyIdxEquals(idx: Long?): BooleanExpression? = idx?.let { this.company().idx.eq(it) }
fun QAdministrator.userNameContains(userName: String?): BooleanExpression? =
    userName?.let { this.userName.containsIgnoreCase(it) }

fun QAdministrator.stateEquals(state: State?): BooleanExpression? = state?.let { this.state.eq(it) }
