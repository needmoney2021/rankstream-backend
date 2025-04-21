package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.dto.tree.MemberTreeDto
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

    @Query(
        nativeQuery = true,
        value = """
            WITH RECURSIVE closure_tree AS (
                SELECT
                    m.idx,
                    m.member_id,
                    m.member_name,
                    m.position,
                    m.grade_idx,
                    0 AS depth
                FROM member m
                WHERE m.idx = :rootIdx
            
                UNION ALL
            
                SELECT
                    c.idx,
                    c.member_id,
                    c.member_name,
                    c.position,
                    c.grade_idx,
                    ct.depth + 1 AS depth
                FROM member_closure mc
                    JOIN closure_tree ct ON mc.ancestor_idx = ct.idx
                    JOIN member c ON mc.descendant_idx = c.idx
                WHERE mc.depth = 1
            )
            
            SELECT
                ct.*,
                mc.ancestor_idx AS parent_idx
            FROM closure_tree ct
                LEFT JOIN member_closure mc ON mc.descendant_idx = ct.idx AND mc.depth = 1
        """
    )
    fun findRecursiveTreeByAncestor(rootIdx: Long): List<MemberTreeDto>
}
