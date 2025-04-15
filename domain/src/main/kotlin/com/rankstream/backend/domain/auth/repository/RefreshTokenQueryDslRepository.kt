package com.rankstream.backend.domain.auth.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {


}
