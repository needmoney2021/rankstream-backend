package com.rankstream.backend.domain.grade.service

import com.rankstream.backend.domain.company.repository.CompanyQueryDslRepository
import com.rankstream.backend.domain.company.repository.CompanyRepository
import com.rankstream.backend.domain.grade.dto.request.GradeRegistrationRequest
import com.rankstream.backend.domain.grade.dto.request.GradeUpdateRequest
import com.rankstream.backend.domain.grade.dto.response.GradeResponse
import com.rankstream.backend.domain.grade.entity.Grade
import com.rankstream.backend.domain.grade.repository.GradeQueryDslRepository
import com.rankstream.backend.domain.grade.repository.GradeRepository
import com.rankstream.backend.exception.ForbiddenException
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.enums.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GradeService(
    private val gradeRepository: GradeRepository,
    private val companyRepository: CompanyRepository,
    private val gradeQueryDslRepository: GradeQueryDslRepository
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GradeService::class.java)
    }

    fun findByCompanyIdx(companyIdx: Long): List<GradeResponse> {
        return gradeQueryDslRepository.findByCompanyIdx(companyIdx)
    }

    fun findGradeByCompanyIdxAndIdx(companyIdx: Long, idx: Long): GradeResponse {
        return gradeQueryDslRepository.findByCompanyIdxAndIdx(companyIdx, idx)
            ?: throw NotFoundException("Grade with idx $idx not found.", ErrorCode.NOT_FOUND)
    }

    @Transactional(readOnly = false)
    fun updateGrade(companyIdx: Long, idx: Long, gradeUpdateRequest: GradeUpdateRequest): GradeResponse {
        val grade = gradeRepository.findByIdx(idx)
            ?: throw NotFoundException("Grade with idx $idx not found.", ErrorCode.NOT_FOUND)

        if (grade.company.idx != companyIdx) {
            throw ForbiddenException("Company idx $companyIdx is not matched with ${grade.company.idx}", ErrorCode.FORBIDDEN)
        }

        with (gradeUpdateRequest) {
            name?.let {
                if (it != grade.gradeName) {
                    grade.gradeName = it
                }
            }
            achievementPoint?.let {
                if (it != grade.requiredPoint) {
                    grade.requiredPoint = it
                }
            }
            refundRate?.let {
                if (it != grade.paybackRatio) {
                    grade.paybackRatio = it
                }
            }
        }

        return GradeResponse.fromEntity(grade)
    }

    @Transactional(readOnly = false)
    fun registerGrade(companyIdx: Long, gradeRegistrationRequest: GradeRegistrationRequest): GradeResponse {
        val company = companyRepository.findByIdx(companyIdx)
            ?: throw NotFoundException("Company with idx $companyIdx not found.", ErrorCode.NOT_FOUND)

        return GradeResponse.fromEntity(gradeRepository.save(Grade.create(gradeRegistrationRequest, company)))
    }
}
