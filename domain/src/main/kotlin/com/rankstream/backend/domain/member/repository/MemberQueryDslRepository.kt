package com.rankstream.backend.domain.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MemberQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) 