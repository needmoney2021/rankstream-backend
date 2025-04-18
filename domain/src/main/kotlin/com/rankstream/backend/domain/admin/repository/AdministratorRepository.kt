package com.rankstream.backend.domain.admin.repository

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AdministratorRepository : JpaRepository<Administrator, Long> {

    fun existsByCompanyAndUserId(company: Company, userId: String): Boolean

    @Query("""
        SELECT a FROM Administrator a JOIN FETCH a.company WHERE a.userId = :userId        
    """)
    fun findByUserId(userId: String): Administrator?

    @Query("""
        SELECT a FROM Administrator a JOIN FETCH a.company WHERE a.idx = :idx
    """)
    fun findByIdx(idx: Long): Administrator?
}
