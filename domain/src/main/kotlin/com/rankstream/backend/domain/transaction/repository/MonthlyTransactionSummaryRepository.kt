package com.rankstream.backend.domain.transaction.repository

import com.rankstream.backend.domain.transaction.entity.MonthlyTransactionSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MonthlyTransactionSummaryRepository : JpaRepository<MonthlyTransactionSummary, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        nativeQuery = true,
        value = """
            DELETE s FROM monthly_transaction_summary s JOIN member m ON s.member_idx = m.idx WHERE m.company_idx = :companyIdx
        """
    )
    fun deleteByCompanyIdx(companyIdx: Long)
}
