package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<Member, Long> {

    @Query("""
        SELECT m FROM Member m JOIN FETCH m.company WHERE m.company = :company
    """)
    fun findByCompany(company: Company): List<Member>

    @Modifying(clearAutomatically = true)
    @Query(
        nativeQuery = true,
        value ="""
            DELETE FROM member WHERE company_idx = :companyIdx
        """)
    fun deleteByCompany(@Param("companyIdx") companyIdx: Long): Int
}
