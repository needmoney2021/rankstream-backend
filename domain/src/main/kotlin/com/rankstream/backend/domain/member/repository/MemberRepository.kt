package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.enums.MemberPosition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<Member, Long> {

    @Query("""
        SELECT m 
        FROM Member m 
        JOIN FETCH m.company 
        JOIN FETCH m.grade
        WHERE m.company = :company
    """)
    fun findByCompany(company: Company): List<Member>

    @Query("""
        SELECT m 
        FROM Member m 
        JOIN FETCH m.company 
        JOIN FETCH m.grade
        WHERE m.company.idx = :companyIdx AND m.idx = :idx
    """)
    fun findByCompanyIdxAndIdx(companyIdx: Long, idx: Long): Member?

    @Query("""
        SELECT m
        FROM Member m
        JOIN FETCH m.company
        JOIN FETCH m.grade
        WHERE m.company.idx = :companyIdx
            AND m.memberId = :memberId
    """)
    fun findByCompanyIdxAndMemberId(companyIdx: Long, memberId: String): Member?

    fun existsByCompanyAndSponsorAndPosition(company: Company, sponsor: Member, position: MemberPosition): Boolean

    @Modifying(clearAutomatically = true)
    @Query(
        nativeQuery = true,
        value ="""
            DELETE FROM member WHERE company_idx = :companyIdx
        """)
    fun deleteByCompany(@Param("companyIdx") companyIdx: Long): Int

    @Query("""
        SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END 
        FROM Member m 
        WHERE m.company = :company
        AND m.state = 'ACTIVE'
        AND m.sponsor IS NULL AND m.recommender IS NULL
    """)
    fun existsByCompany(company: Company): Boolean
}
