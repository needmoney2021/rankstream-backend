package com.rankstream.backend.internalapi.controller.member

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.enums.Gender
import com.rankstream.backend.domain.member.dto.request.MemberSearchRequest
import com.rankstream.backend.domain.member.dto.response.MemberResponse
import com.rankstream.backend.domain.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
