package com.rankstream.backend.domain.transaction.repository

import com.rankstream.backend.domain.transaction.entity.MonthlyTransactionSummary
import org.springframework.data.jpa.repository.JpaRepository

interface MonthlyTransactionSummaryRepository : JpaRepository<MonthlyTransactionSummary, Long> 