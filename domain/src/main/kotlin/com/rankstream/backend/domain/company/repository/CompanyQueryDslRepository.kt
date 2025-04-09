package com.rankstream.backend.domain.company.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CompanyQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) 