package com.rankstream.backend.internalapi.controller.admin

import com.rankstream.backend.auth.extensions.withPasswordEncrypt
import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.admin.dto.request.AdministratorRegistrationRequest
import com.rankstream.backend.domain.admin.dto.request.AdministratorSearchRequest
import com.rankstream.backend.domain.admin.dto.request.AdministratorUpdateRequest
import com.rankstream.backend.domain.admin.dto.response.AdministratorResponse
import com.rankstream.backend.domain.admin.service.AdministratorService
import com.rankstream.backend.domain.enums.State
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

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
    ): ResponseEntity<List<AdministratorResponse>> {
        val administratorSearchRequest = AdministratorSearchRequest(name, id, state, administratorDetails.administrator.company.idx!!)
        return ResponseEntity.ok(administratorService.findAdministrators(administratorSearchRequest))
    }

    @GetMapping("/{idx}")
    fun findAdministratorDetails(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @PathVariable("idx") idx: Long
    ): ResponseEntity<AdministratorResponse> {
        return ResponseEntity.ok(administratorService.findByIdx(administratorDetails.administrator.company.idx!!, idx))
    }

    @PatchMapping("/{idx}")
    fun updateAdministrator(
        @RequestBody @Validated administratorUpdateRequest: AdministratorUpdateRequest,
        @PathVariable("idx") idx: Long,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<AdministratorResponse> {
        return ResponseEntity.ok(administratorService.updateAdministrator(administratorUpdateRequest.withPasswordEncrypt(), idx, administratorDetails.administrator.company.idx!!))
    }

    @PostMapping("")
    fun registerAdministrator(
        @RequestBody @Validated administratorRegisterRequest: AdministratorRegistrationRequest,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ) :ResponseEntity<AdministratorResponse> {
        val administrator = administratorService.registerAdministrator(administratorRegisterRequest.withPasswordEncrypt(), administratorDetails.administrator.company.idx!!)
        return ResponseEntity.created(URI.create("/administrators/${administrator.idx}"))
            .body(administrator)
    }
}
