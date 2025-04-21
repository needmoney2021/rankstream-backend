package com.rankstream.backend.internalapi.controller.member

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.member.dto.request.MemberRegistrationRequest
import com.rankstream.backend.domain.member.dto.request.MemberSearchRequest
import com.rankstream.backend.domain.member.dto.request.MemberUpdateRequest
import com.rankstream.backend.domain.member.dto.response.MemberResponse
import com.rankstream.backend.domain.member.dto.response.MemberTreeResponse
import com.rankstream.backend.domain.member.dto.response.RecommenderSponsorValidationResponse
import com.rankstream.backend.domain.member.enums.MemberPosition
import com.rankstream.backend.domain.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("")
    fun findMembers(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @RequestParam(name = "id", required = false) id: String? = null,
        @RequestParam(name = "name", required = false) name: String? = null,
        @RequestParam(name = "gender", required = false) gender: Gender? = null
    ): ResponseEntity<List<MemberResponse>> {
        val memberSearchRequest = MemberSearchRequest(id, name, gender)
        return ResponseEntity.ok(memberService.findMembersByCompanyIdx(administratorDetails.administrator.company.idx!!, memberSearchRequest))
    }

    @GetMapping("/{member-idx}")
    fun findMemberByMemberId(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @PathVariable("member-idx") idx: Long
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.findMemberByCompanyIdxAndIdx(administratorDetails.administrator.company.idx!!, idx))
    }

    @PatchMapping("/{member-idx}")
    fun updateMember(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @PathVariable("member-idx") idx: Long,
        @RequestBody @Validated memberUpdateRequest: MemberUpdateRequest
    ) : ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.updateMemberByCompanyIdxAndMemberIdx(administratorDetails.administrator.company.idx!!, idx, memberUpdateRequest))
    }


    @GetMapping("/recommender/{member-id}")
    fun findRecommender(
        @PathVariable("member-id") id: String,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<RecommenderSponsorValidationResponse> {
        return ResponseEntity.ok(memberService.isRecommenderAvailable(administratorDetails.administrator.company.idx!!, id))
    }

    @GetMapping("/sponsor/{member-id}")
    fun findSponsorAndValidPosition(
        @PathVariable("member-id") id: String,
        @RequestParam(name = "position", required = false) position: MemberPosition,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<RecommenderSponsorValidationResponse> {
        return ResponseEntity.ok(memberService.isSponsorPositionAvailable(administratorDetails.administrator.company.idx!!, id, position))
    }

    @PostMapping("")
    fun postMapping(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @RequestBody @Validated memberRegistrationRequest: MemberRegistrationRequest
    ): ResponseEntity<MemberResponse> {
        val member = memberService.registerMember(administratorDetails.administrator.company.idx!!, memberRegistrationRequest)
        return ResponseEntity.created(URI.create("/members/${member.idx}"))
            .body(member)
    }

    @GetMapping("/{idx}/tree")
    fun getMemberTree(
        @PathVariable("idx") idx: Long,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<MemberTreeResponse> {
        return ResponseEntity.ok(memberService.getMemberTree(administratorDetails.administrator.company.idx!!, idx))
    }
}
