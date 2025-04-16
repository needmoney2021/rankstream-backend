package com.rankstream.backend.internalapi.controller.admin

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.admin.dto.request.AdministratorSearchRequest
import com.rankstream.backend.domain.admin.dto.response.AdministratorSearchResponse
import com.rankstream.backend.domain.admin.service.AdministratorService
import com.rankstream.backend.domain.enums.State
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/administrators")
class AdministratorController(
    private val administratorService: AdministratorService
) {

    @GetMapping("")
    fun findAdministrators(
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) id: String? = null,
        @RequestParam(required = false) state: State? = null,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<List<AdministratorSearchResponse>> {
        val administratorSearchRequest = AdministratorSearchRequest(name, id, state, administratorDetails.administrator.company.idx!!)
        return ResponseEntity.ok(administratorService.findAdministrators(administratorSearchRequest))
    }
}
