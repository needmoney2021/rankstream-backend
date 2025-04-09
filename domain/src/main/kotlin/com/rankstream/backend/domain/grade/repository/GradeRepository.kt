package com.rankstream.backend.domain.grade.repository

import com.rankstream.backend.domain.grade.entity.Grade
import org.springframework.data.jpa.repository.JpaRepository

interface GradeRepository : JpaRepository<Grade, Long> 