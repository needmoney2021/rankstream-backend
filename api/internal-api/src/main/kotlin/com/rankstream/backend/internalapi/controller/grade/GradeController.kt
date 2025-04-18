package com.rankstream.backend.internalapi.controller.grade

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.grade.dto.request.GradeRegistrationRequest
import com.rankstream.backend.domain.grade.dto.request.GradeUpdateRequest
import com.rankstream.backend.domain.grade.dto.response.GradeResponse
import com.rankstream.backend.domain.grade.service.GradeService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/grade")
class GradeController(
    private val gradeService: GradeService
) {

    @GetMapping("")
    fun findAllGrade(@AuthenticationPrincipal administratorDetails: AdministratorDetails): ResponseEntity<List<GradeResponse>> {
        return ResponseEntity.ok(gradeService.findByCompanyIdx(administratorDetails.administrator.company.idx!!))
    }

    @GetMapping("/{idx}")
    fun findGradeByCompanyAndIdx(
        @PathVariable("idx") idx: Long,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails
    ): ResponseEntity<GradeResponse> {
        return ResponseEntity.ok(gradeService.findGradeByCompanyIdxAndIdx(administratorDetails.administrator.company.idx!!, idx))
    }

    @PatchMapping("/{idx}")
    fun updateGrade(
        @PathVariable("idx") idx: Long,
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @RequestBody @Validated gradeUpdateRequest: GradeUpdateRequest
    ): ResponseEntity<GradeResponse> {
        return ResponseEntity.ok(gradeService.updateGrade(administratorDetails.administrator.company.idx!!, idx, gradeUpdateRequest))
    }

    @PostMapping("")
    fun registerGrade(
        @AuthenticationPrincipal administratorDetails: AdministratorDetails,
        @RequestBody @Validated gradeRegistrationRequest: GradeRegistrationRequest
    ): ResponseEntity<GradeResponse> {
        val grade = gradeService.registerGrade(administratorDetails.administrator.company.idx!!, gradeRegistrationRequest)
        return ResponseEntity.created(URI.create("/grade/${grade.idx}"))
            .body(grade)
    }
}
