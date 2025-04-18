package com.rankstream.backend.domain.admin.service

import com.rankstream.backend.domain.admin.dto.request.AdministratorRegistrationCommand
import com.rankstream.backend.domain.admin.dto.request.AdministratorSearchRequest
import com.rankstream.backend.domain.admin.dto.request.AdministratorUpdateCommand
import com.rankstream.backend.domain.admin.dto.response.AdministratorResponse
import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.repository.AdministratorQueryDslRepository
import com.rankstream.backend.domain.admin.repository.AdministratorRepository
import com.rankstream.backend.domain.company.repository.CompanyQueryDslRepository
import com.rankstream.backend.domain.company.repository.CompanyRepository
import com.rankstream.backend.exception.DuplicatedException
import com.rankstream.backend.exception.ForbiddenException
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdministratorService(
    private val administratorRepository: AdministratorRepository,
    private val administratorQueryDslRepository: AdministratorQueryDslRepository,
    private val companyRepository: CompanyRepository
) {

    fun findByUserId(userId: String): Administrator {
        return administratorQueryDslRepository.findByUserId(userId)
            ?: throw NotFoundException("Administrator with id $userId not found.", ErrorCode.NOT_FOUND, userId)
    }

    fun findAdministrators(administratorSearchRequest: AdministratorSearchRequest): List<AdministratorResponse> {
        return administratorQueryDslRepository.findAdministratorsByCondition(administratorSearchRequest)
    }

    fun findByIdx(companyIdx: Long, idx: Long): AdministratorResponse {
        return administratorQueryDslRepository.findByCompanyAndIdx(companyIdx, idx)
            ?: throw NotFoundException("Administrator with id $idx and company $companyIdx not found.", ErrorCode.NOT_FOUND, listOf(idx, companyIdx))
    }

    @Transactional(readOnly = false)
    fun updateAdministrator(administratorUpdateRequestWithPasswordEncoded: AdministratorUpdateCommand, idx: Long, companyIdx: Long): AdministratorResponse {
        // FIXME 엔티티 조회가 필요한 부분은 JpaRepository로
        val administrator = administratorQueryDslRepository.findByIdx(idx)
            ?: throw NotFoundException("Administrator with id $idx not found.", ErrorCode.NOT_FOUND, idx)
        if (administrator.company.idx != companyIdx) {
            throw ForbiddenException("Company idx $companyIdx is not matched with ${administrator.company.idx}.", ErrorCode.FORBIDDEN)
        }

        with (administratorUpdateRequestWithPasswordEncoded) {
            state?.let {
                if (it != administrator.state) {
                    administrator.state = it
                }
            }
            department?.let {
                if (it != administrator.department) {
                    administrator.department = it
                }
            }
            newEncodedPassword?.let {
                administrator.password = it
            }
        }

        return AdministratorResponse.fromEntity(administrator)
    }

    @Transactional(readOnly = false)
    fun registerAdministrator(administratorRegistrationRequestWithPasswordEncoded: AdministratorRegistrationCommand, companyIdx: Long): AdministratorResponse {
        val company = companyRepository.findByIdx(companyIdx)
            ?: throw NotFoundException("Company with id $companyIdx not found.", ErrorCode.NOT_FOUND, companyIdx)

        val exists = administratorRepository.existsByCompanyAndUserId(company, administratorRegistrationRequestWithPasswordEncoded.id)
        if (exists) {
            throw DuplicatedException("Administrator with id ${administratorRegistrationRequestWithPasswordEncoded.id} already exists in company $companyIdx.")
        }

        return AdministratorResponse.fromEntity(administratorRepository.save(Administrator.create(administratorRegistrationRequestWithPasswordEncoded, company)))
    }
}
