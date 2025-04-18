package com.rankstream.backend.domain.member.repository

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
}
