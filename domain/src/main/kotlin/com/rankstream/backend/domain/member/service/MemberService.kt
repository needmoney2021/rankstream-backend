package com.rankstream.backend.domain.member.service

import com.rankstream.backend.domain.member.dto.request.MemberSearchRequest
import com.rankstream.backend.domain.member.dto.response.MemberResponse
import com.rankstream.backend.domain.member.repository.MemberQueryDslRepository
import com.rankstream.backend.domain.member.repository.MemberRepository
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.enums.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberQueryDslRepository: MemberQueryDslRepository
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MemberService::class.java)
    }

    fun findMembersByCompanyIdx(companyIdx: Long, memberSearchRequest: MemberSearchRequest): List<MemberResponse> {
        return memberQueryDslRepository.findByCompanyIdx(companyIdx, memberSearchRequest)
    }

    fun findMemberByCompanyIdxAndIdx(companyIdx: Long, idx: Long): MemberResponse {
        return memberQueryDslRepository.findDetailByMemberIdxAndCompanyIdx(idx, companyIdx)
            ?: throw NotFoundException("Member not found with idx $idx and company $companyIdx", ErrorCode.MEMBER_NOT_FOUND, listOf(idx, companyIdx))
    }
}
