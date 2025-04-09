package com.rankstream.backend.domain.schedule.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ScheduleQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) 