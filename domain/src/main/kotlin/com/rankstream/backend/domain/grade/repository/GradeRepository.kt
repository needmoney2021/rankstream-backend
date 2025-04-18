package com.rankstream.backend.domain.grade.repository

import com.rankstream.backend.domain.grade.entity.Grade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GradeRepository : JpaRepository<Grade, Long> {
    @Query("""
        SELECT g FROM Grade g JOIN FETCH g.company
        WHERE g.idx = :idx
    """)
    fun findByIdx(idx: Long): Grade?
}
