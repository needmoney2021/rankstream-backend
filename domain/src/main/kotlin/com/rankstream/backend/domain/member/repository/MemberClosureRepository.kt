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
                    m.idx, m.member_id, m.member_name, m.position, m.grade_idx, 0 AS depth
                FROM member m
                WHERE m.idx = :rootIdx
                
                UNION ALL
                
                SELECT
                    child.idx, child.member_id, child.member_name, child.position, child.grade_idx, parent.depth + 1 AS depth
                FROM member child
                JOIN member_closure mc ON mc.descendant_idx = child.idx
                JOIN closure_tree parent ON mc.ancestor_idx = parent.idx AND mc.depth = 1
            )
            SELECT * FROM closure_tree
        """
    )
    fun findRecursiveTreeByAncestor(rootIdx: Long): List<MemberTreeDto>
}
