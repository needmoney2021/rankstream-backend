package com.rankstream.backend.domain.signup

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.repository.AdministratorRepository
import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import com.rankstream.backend.domain.company.dto.response.CompanyRegistrationResponse
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.company.repository.CompanyQueryDslRepository
import com.rankstream.backend.domain.company.repository.CompanyRepository
import com.rankstream.backend.exception.DuplicatedException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SignupService(
    private val companyRepository: CompanyRepository,
    private val companyQueryDslRepository: CompanyQueryDslRepository,
    private val administratorRepository: AdministratorRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(SignupService::class.java)
    }

    @Transactional(readOnly = false)
    fun signup(companyRegistrationRequest: CompanyRegistrationRequest): CompanyRegistrationResponse {
        log.info("Received company registration request: {}", companyRegistrationRequest)
        val exists = companyQueryDslRepository.findByBusinessLicense(companyRegistrationRequest.businessLicense)
        if (exists != null) {
            throw DuplicatedException("Company with business license ${companyRegistrationRequest.businessLicense} already exists.")
        }
        val company = companyRepository.save(Company.fromCompanyRegistration(companyRegistrationRequest))
        val administrator = administratorRepository.save(Administrator.fromCompanyRegistration(companyRegistrationRequest, company))

        return CompanyRegistrationResponse.fromCompanyAndAdministrator(company, administrator)
    }

}
