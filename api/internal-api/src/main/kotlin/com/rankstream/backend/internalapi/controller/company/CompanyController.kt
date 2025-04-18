package com.rankstream.backend.internalapi.controller.company

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.company.dto.request.CompanyCommissionRequest
import com.rankstream.backend.domain.company.dto.response.CompanyCommissionResponse
import com.rankstream.backend.domain.company.service.CompanyService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyController(
    private val companyService: CompanyService
) {

    @GetMapping("/commission")
    fun findCompanyCommissionPlan(@AuthenticationPrincipal administratorDetails: AdministratorDetails): ResponseEntity<CompanyCommissionResponse> {
        return ResponseEntity.ok(companyService.findCompanyCommissionPlan(administratorDetails.administrator.company.idx!!))
    }

    @PatchMapping("/commission")
    fun updateCompanyCommissionPlan(
        @RequestBody @Validated companyCommissionRequest: CompanyCommissionRequest,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ) : ResponseEntity<CompanyCommissionResponse> {
        TODO()
    }
}
