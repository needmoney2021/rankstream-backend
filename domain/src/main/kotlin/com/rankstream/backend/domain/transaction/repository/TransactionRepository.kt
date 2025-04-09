package com.rankstream.backend.domain.transaction.repository

import com.rankstream.backend.domain.transaction.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> 