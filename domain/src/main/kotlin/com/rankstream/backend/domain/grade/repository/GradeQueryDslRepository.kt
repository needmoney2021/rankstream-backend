package com.rankstream.backend.domain.grade.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class GradeQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) 