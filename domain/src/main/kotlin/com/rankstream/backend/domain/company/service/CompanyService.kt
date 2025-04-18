package com.rankstream.backend.domain.company.service

import com.rankstream.backend.domain.company.dto.response.CompanyCommissionResponse
import com.rankstream.backend.domain.company.repository.CompanyRepository
import com.rankstream.backend.domain.member.repository.MemberClosureRepository
import com.rankstream.backend.domain.member.repository.MemberRepository
import com.rankstream.backend.domain.transaction.repository.TransactionRepository
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.enums.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val transactionRepository: TransactionRepository,
    private val memberRepository: MemberRepository,
    private val memberClosureRepository: MemberClosureRepository,
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CompanyService::class.java)
    }

    fun findCompanyCommissionPlan(companyIdx: Long): CompanyCommissionResponse {
        val company = companyRepository.findByIdx(companyIdx)
            ?: throw NotFoundException("Company not found with $companyIdx", ErrorCode.NOT_FOUND, companyIdx)

        return CompanyCommissionResponse(company.commissionPlan)
    }
}
