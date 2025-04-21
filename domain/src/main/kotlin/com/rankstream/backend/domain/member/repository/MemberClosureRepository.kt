package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.entity.MemberClosure
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberClosureRepository : JpaRepository<MemberClosure, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        nativeQuery = true,
        value ="""
            DELETE FROM member_closure
            WHERE ancestor_idx IN (
                SELECT idx FROM member WHERE company_idx = :companyIdx
            )
            OR descendant_idx IN (
                SELECT idx FROM member WHERE company_idx = :companyIdx
            )
        """)
    fun deleteClosuresByCompany(@Param("companyIdx") companyIdx: Long): Int

    @Query("""
        SELECT m FROM MemberClosure m 
        JOIN FETCH m.ancestor
        JOIN FETCH m.descendant
        WHERE m.descendant = :member
    """)
    fun findByDescendant(member: Member): List<MemberClosure>

    @Query("""
        SELECT m FROM MemberClosure m
        JOIN FETCH m.ancestor
        JOIN FETCH m.descendant
        WHERE m.ancestor = :member AND m.depth > 0
        ORDER BY m.depth
    """)
    fun findByAncestor(member: Member): List<MemberClosure>
}
