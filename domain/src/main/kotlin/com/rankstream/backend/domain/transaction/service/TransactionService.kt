package com.rankstream.backend.domain.transaction.service

import com.rankstream.backend.domain.transaction.dto.request.TransactionSearchRequest
import com.rankstream.backend.domain.transaction.dto.response.TransactionResponse
import com.rankstream.backend.domain.transaction.repository.TransactionQueryDslRepository
import com.rankstream.backend.exception.BadRequestException
import com.rankstream.backend.exception.enums.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionQueryDslRepository: TransactionQueryDslRepository
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(TransactionService::class.java)
    }

    fun findTransactionsByCompanyIdxAndConditions(companyIdx: Long, transactionSearchRequest: TransactionSearchRequest): List<TransactionResponse> {
        val orderedFrom = transactionSearchRequest.orderedFrom
        val orderedTo = transactionSearchRequest.orderedTo
        if (orderedFrom.isBefore(orderedTo.minusDays(31L))) {
            log.error("Ordered from date is before 30 days ago.")
            throw BadRequestException("Ordered from date is before 30 days ago.", ErrorCode.DATE_RANGE_TOO_WIDE)
        }
        if (orderedFrom.isAfter(orderedTo)) {
            throw BadRequestException("Ordered from date is after ordered to date.", ErrorCode.DATE_RANGE_INVALID)
        }
        return transactionQueryDslRepository.findTransactionsByCompanyIdxAndConditions(companyIdx, transactionSearchRequest)
    }
}
