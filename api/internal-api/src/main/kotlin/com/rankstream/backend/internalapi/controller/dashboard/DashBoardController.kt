package com.rankstream.backend.internalapi.controller.dashboard

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.member.dto.response.MemberStatisticsResponse
import com.rankstream.backend.domain.member.service.MemberStatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashBoardController(
    private val memberStatisticsService: MemberStatisticsService
) {

    @RequestMapping("/member-statistics")
    fun getMemberStatistics(@AuthenticationPrincipal administratorDetails: AdministratorDetails): ResponseEntity<List<MemberStatisticsResponse>> {
        return ResponseEntity.ok(memberStatisticsService.findLastThirtyDaysStatisticsByCompanyIdx(administratorDetails.administrator.company.idx!!))
    }
}
