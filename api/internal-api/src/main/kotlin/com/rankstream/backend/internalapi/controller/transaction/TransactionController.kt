package com.rankstream.backend.internalapi.controller.transaction

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.transaction.dto.request.ClosedTransactionSearchRequest
import com.rankstream.backend.domain.transaction.dto.request.TransactionSearchRequest
import com.rankstream.backend.domain.transaction.dto.response.ClosedTransactionResponse
import com.rankstream.backend.domain.transaction.dto.response.TransactionResponse
import com.rankstream.backend.domain.transaction.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @GetMapping("")
    fun findTransactionsByCompanyIdxAndConditions(
        @RequestParam("member-id") memberId: String? = null,
        @RequestParam("transaction-id") transactionId: String? = null,
        @RequestParam("ordered-from") orderedFrom: LocalDate,
        @RequestParam("ordered-to") orderedTo: LocalDate,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<List<TransactionResponse>> {
        val transactionSearchRequest =
            TransactionSearchRequest(
                memberId,
                transactionId,
                LocalDateTime.of(orderedFrom, LocalTime.of(0, 0, 0)),
                LocalDateTime.of(orderedTo, LocalTime.of(23, 59, 59))
            )
        return ResponseEntity.ok(transactionService.findTransactionsByCompanyIdxAndConditions(administratorDetails.administrator.company.idx!!, transactionSearchRequest))
    }

    @GetMapping("/closed")
    fun findClosedTransactionsByCompanyIdxAndConditions(
        @RequestParam("member-id") memberId: String? = null,
        @RequestParam("start-year") startYear: Int,
        @RequestParam("start-month") startMonth: Int,
        @RequestParam("end-year") endYear: Int,
        @RequestParam("end-month") endMonth: Int,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<List<ClosedTransactionResponse>> {
        val closedTransactionSearchRequest = ClosedTransactionSearchRequest(startYear, startMonth, endYear, endMonth, memberId)
        return ResponseEntity.ok(transactionService.findClosedTransactionByCompanyIdxAndConditions(administratorDetails.administrator.company.idx!!, closedTransactionSearchRequest))
    }
}
