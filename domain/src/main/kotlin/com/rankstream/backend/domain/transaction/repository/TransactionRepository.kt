package com.rankstream.backend.domain.transaction.repository

import com.rankstream.backend.domain.transaction.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TransactionRepository : JpaRepository<Transaction, Long> {
    @Modifying(clearAutomatically = true)
    @Query(
        nativeQuery = true,
        value = """
            DELETE FROM `transaction`
            WHERE member_idx IN (
                SELECT idx FROM member WHERE company_idx = :companyIdx
            )
        """)
    fun deleteTransactionsByCompany(@Param("companyIdx") companyIdx: Long): Int
}
