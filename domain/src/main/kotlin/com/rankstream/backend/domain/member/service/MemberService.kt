package com.rankstream.backend.domain.member.service

import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.company.enums.CommissionPlan
import com.rankstream.backend.domain.company.repository.CompanyRepository
import com.rankstream.backend.domain.grade.repository.GradeRepository
import com.rankstream.backend.domain.member.dto.request.MemberRegistrationRequest
import com.rankstream.backend.domain.member.dto.request.MemberSearchRequest
import com.rankstream.backend.domain.member.dto.request.MemberUpdateRequest
import com.rankstream.backend.domain.member.dto.response.BinaryTreeNode
import com.rankstream.backend.domain.member.dto.response.GeneralTreeNode
import com.rankstream.backend.domain.member.dto.response.MemberResponse
import com.rankstream.backend.domain.member.dto.response.MemberTreeResponse
import com.rankstream.backend.domain.member.dto.response.RecommenderSponsorValidationResponse
import com.rankstream.backend.domain.member.dto.response.TreeLink
import com.rankstream.backend.domain.member.entity.Member
import com.rankstream.backend.domain.member.entity.MemberClosure
import com.rankstream.backend.domain.member.entity.MemberGradeHistory
import com.rankstream.backend.domain.member.enums.MemberPosition
import com.rankstream.backend.domain.member.repository.MemberClosureRepository
import com.rankstream.backend.domain.member.repository.MemberGradeHistoryRepository
import com.rankstream.backend.domain.member.repository.MemberQueryDslRepository
import com.rankstream.backend.domain.member.repository.MemberRepository
import com.rankstream.backend.exception.BadRequestException
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
    private val memberQueryDslRepository: MemberQueryDslRepository,
    private val memberGradeHistoryRepository: MemberGradeHistoryRepository,
    private val gradeRepository: GradeRepository,
    private val companyRepository: CompanyRepository,
    private val memberClosureRepository: MemberClosureRepository
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MemberService::class.java)
    }

    fun findMembersByCompanyIdx(companyIdx: Long, memberSearchRequest: MemberSearchRequest): List<MemberResponse> {
        return memberQueryDslRepository.findByCompanyIdx(companyIdx, memberSearchRequest)
    }

    fun findMemberByCompanyIdxAndIdx(companyIdx: Long, idx: Long): MemberResponse {
        return memberQueryDslRepository.findDetailByMemberIdxAndCompanyIdx(idx, companyIdx)
            ?: notFound(idx, companyIdx)
    }


    fun isRecommenderAvailable(companyIdx: Long, recommenderId: String): RecommenderSponsorValidationResponse {
        val recommender = memberRepository.findByCompanyIdxAndMemberId(companyIdx, recommenderId)
            ?: notFound(recommenderId, companyIdx)

        // 활성 상태 체크
        val isActive = recommender.isActive()
        return validationResponse(
            isActive,
            if (isActive) { null } else { ErrorCode.MEMBER_DEACTIVATED }
        )
    }

    fun isSponsorPositionAvailable(companyIdx: Long, sponsorId: String, position: MemberPosition): RecommenderSponsorValidationResponse {
        val sponsor = memberRepository.findByCompanyIdxAndMemberId(companyIdx, sponsorId)
            ?: notFound(sponsorId, companyIdx)

        // 활성 상태 체크
        if (!sponsor.isActive()) return validationResponse(false, ErrorCode.MEMBER_DEACTIVATED)

        // 해당 포지션에 이미 자식이 있으면 false
        val isOccupied = memberRepository.existsByCompanyAndSponsorAndPosition(sponsor.company, sponsor, position)
        if (isOccupied) return validationResponse(false, ErrorCode.SLOT_IS_OCCUPIED)

        return validationResponse(true)
    }

    @Transactional(readOnly = false)
    fun updateMemberByCompanyIdxAndMemberIdx(
        companyIdx: Long,
        memberIdx: Long,
        memberUpdateRequest: MemberUpdateRequest
    ): MemberResponse {
        val member = memberRepository.findByCompanyIdxAndIdx(companyIdx, memberIdx)
            ?: throw NotFoundException("Member not found with idx $memberIdx and company $companyIdx", ErrorCode.MEMBER_NOT_FOUND, listOf(memberIdx, companyIdx))

        if (applyChanges(member, memberUpdateRequest)) {
            memberRepository.save(member)
        }

        return memberQueryDslRepository.findDetailByMemberIdxAndCompanyIdx(memberIdx, companyIdx)
            ?: throw NotFoundException("Updated member not found", ErrorCode.MEMBER_NOT_FOUND)
    }

    private fun applyChanges(member: Member, request: MemberUpdateRequest): Boolean {
        var updated = false

        request.gradeIdx?.takeIf { it != member.grade.idx }?.let { newGradeIdx ->
            val newGrade = gradeRepository.findByIdx(newGradeIdx)
                ?: throw NotFoundException("Grade not found with idx $newGradeIdx", ErrorCode.NOT_FOUND)
            memberGradeHistoryRepository.save(
                MemberGradeHistory(
                    previous = member.grade,
                    changed = newGrade,
                    member = member
                )
            )
            member.grade = newGrade
            updated = true
        }

        request.state?.takeIf { it != member.state }?.let { newState ->
            member.state = newState
            updated = true
        }

        return updated
    }

    @Transactional(readOnly = false)
    fun registerMember(companyIdx: Long, memberRegistrationRequest: MemberRegistrationRequest): MemberResponse {
        val company = companyRepository.findByIdx(companyIdx)
            ?: notFound(companyIdx)

        val isGenesis = memberRegistrationRequest.isGenesis
        if (isGenesis && memberRepository.existsByCompany(company)) {
            throw BadRequestException("Company already has members", ErrorCode.GENESIS_MEMBER_ALREADY_EXISTS)
        }

        val grade = gradeRepository.findByIdx(memberRegistrationRequest.gradeIdx)
            ?: throw NotFoundException("Grade not found with idx ${memberRegistrationRequest.gradeIdx}", ErrorCode.NOT_FOUND)

        val recommender = memberRegistrationRequest.recommenderId?.let {
            memberRepository.findByCompanyIdxAndMemberId(companyIdx, it)
                ?: notFound(it, companyIdx)
        }
        val sponsor = memberRegistrationRequest.sponsorId?.let {
            memberRepository.findByCompanyIdxAndMemberId(companyIdx, it)
                ?: notFound(it, companyIdx)
        }

        val member = Member.create(company, grade, recommender, sponsor, memberRegistrationRequest)
        memberRegistrationRequest.joinedAt?.let { member.createdAt = it }
        val savedMember = memberRepository.save(member)

        saveMemberClosure(savedMember, sponsor)

        return memberQueryDslRepository.findDetailByMemberIdxAndCompanyIdx(savedMember.idx!!, company.idx!!)!!
    }

    fun getMemberTree(companyIdx: Long, idx: Long): MemberTreeResponse {
        val company = companyRepository.findByIdx(companyIdx)
            ?: notFound(companyIdx)
        val rootMember = memberRepository.findByCompanyIdxAndIdx(companyIdx, idx)
            ?: notFound(idx, companyIdx)
        val plan = company.commissionPlan
            ?: throw IllegalStateException("Company commission plan is null")

        return if (CommissionPlan.isBinary(plan)) {
            buildBinaryTree(rootMember, company)
        } else {
            buildGeneralTree(rootMember, company)
        }
    }

    private fun buildBinaryTree(rootMember: Member, company: Company): MemberTreeResponse {
        val allGrades = gradeRepository.findByCompany(company)
        val closures = memberClosureRepository.findByAncestor(rootMember)
            .filter { it.depth > 0 }

        val memberDepthMap = closures.groupBy { it.descendant.idx!! }
            .mapValues { it.value.minOf { closure -> closure.depth } }

        val members = closures.map { it.descendant }.distinctBy { it.idx }

        val nodeMap = members.associateBy({ it.idx!! }) { member ->
            BinaryTreeNode(
                idx = member.idx!!,
                id = member.memberId,
                name = member.memberName,
                depth = memberDepthMap[member.idx!!] ?: 1,
                gradeLevel = allGrades.indexOf(member.grade) + 1
            )
        }.toMutableMap()

        val rootNode = BinaryTreeNode(
            idx = rootMember.idx!!,
            id = rootMember.memberId,
            name = rootMember.memberName,
            depth = 0,
            gradeLevel = allGrades.indexOf(rootMember.grade) + 1
        )
        nodeMap[rootMember.idx!!] = rootNode

        val closureMap = closures.groupBy { it.ancestor.idx!! }
        val links = mutableListOf<TreeLink>()
        connectBinaryTree(rootNode, closureMap, nodeMap, links)

        return MemberTreeResponse(
            nodes = listOf(rootNode),
            links = links,
            isBinary = true
        )
    }

    private fun connectBinaryTree(
        parent: BinaryTreeNode,
        closureMap: Map<Long, List<MemberClosure>>,
        nodeMap: Map<Long, BinaryTreeNode>,
        links: MutableList<TreeLink>
    ) {
        closureMap[parent.idx]?.forEach { closure ->
            val child = nodeMap[closure.descendant.idx!!]
                ?: throw IllegalStateException("Node not found for child")
            val position = closure.descendant.position
                ?: throw IllegalStateException("Binary tree member without position")

            parent.children[position] = child
            links.add(TreeLink(source = parent.idx, target = child.idx))
            connectBinaryTree(child, closureMap, nodeMap, links)
        }
    }

    private fun buildGeneralTree(rootMember: Member, company: Company): MemberTreeResponse {
        val allGrades = gradeRepository.findByCompany(company)
        val closures = memberClosureRepository.findByAncestor(rootMember)
            .filter { it.depth > 0 }

        val memberDepthMap = closures.groupBy { it.descendant.idx!! }
            .mapValues { it.value.minOf { closure -> closure.depth } }

        val members = closures.map { it.descendant }.distinctBy { it.idx }

        val nodeMap = members.associateBy({ it.idx!! }) { member ->
            GeneralTreeNode(
                idx = member.idx!!,
                id = member.memberId,
                name = member.memberName,
                depth = memberDepthMap[member.idx!!] ?: 1,
                gradeLevel = allGrades.indexOf(member.grade) + 1
            )
        }.toMutableMap()

        val rootNode = GeneralTreeNode(
            idx = rootMember.idx!!,
            id = rootMember.memberId,
            name = rootMember.memberName,
            depth = 0,
            gradeLevel = allGrades.indexOf(rootMember.grade) + 1
        )
        nodeMap[rootMember.idx!!] = rootNode

        val closureMap = closures.groupBy { it.ancestor.idx!! }
        val links = mutableListOf<TreeLink>()
        connectGeneralTree(rootNode, closureMap, nodeMap, links)

        return MemberTreeResponse(
            nodes = listOf(rootNode),
            links = links,
            isBinary = false
        )
    }

    private fun connectGeneralTree(
        parent: GeneralTreeNode,
        closureMap: Map<Long, List<MemberClosure>>,
        nodeMap: Map<Long, GeneralTreeNode>,
        links: MutableList<TreeLink>
    ) {
        closureMap[parent.idx]?.forEach { closure ->
            val child = nodeMap[closure.descendant.idx!!]
                ?: throw IllegalStateException("Node not found for child")
            parent.children.add(child)
            links.add(TreeLink(source = parent.idx, target = child.idx))
            connectGeneralTree(child, closureMap, nodeMap, links)
        }
    }



    private fun saveMemberClosure(member: Member, sponsor: Member?) {
        val closures = mutableListOf<MemberClosure>()
        // 자기자신 클로저 등록
        closures.add(MemberClosure(
            ancestor = member, descendant = member, depth = 0
        ))

        // 상위 스폰서가 있을때 상위 스폰서가 자손인 클로저를 모두 검색 -> depth를 1 더한 member 엔티티를 생성
        sponsor?.let {
            val sponsorClosures = memberClosureRepository.findByDescendant(it)
            closures.addAll(
                sponsorClosures.map { closure ->
                    MemberClosure(
                        ancestor = closure.ancestor, descendant = member, depth = closure.depth + 1
                    )
                }
            )
        }
        memberClosureRepository.saveAll(closures)
    }

    private fun notFound(companyIdx: Long): Nothing {
        throw NotFoundException("Company not found with idx $companyIdx", ErrorCode.NOT_FOUND)
    }

    private fun notFound(memberId: String, companyIdx: Long): Nothing {
        throw NotFoundException(
            "Member not found with id $memberId and company $companyIdx",
            ErrorCode.MEMBER_NOT_FOUND,
            listOf(memberId, companyIdx)
        )
    }

    private fun notFound(idx: Long, companyIdx: Long): Nothing {
        throw NotFoundException(
            "Member not found with idx $idx and company $companyIdx",
            ErrorCode.MEMBER_NOT_FOUND,
            listOf(idx, companyIdx)
        )
    }


    private fun validationResponse(valid: Boolean, errorCode: ErrorCode? = null): RecommenderSponsorValidationResponse {
        return RecommenderSponsorValidationResponse(
            valid = valid,
            reason = errorCode?.message
        )
    }


}
