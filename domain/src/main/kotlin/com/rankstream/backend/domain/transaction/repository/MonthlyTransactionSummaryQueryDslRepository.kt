package com.rankstream.backend.domain.transaction.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MonthlyTransactionSummaryQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) 