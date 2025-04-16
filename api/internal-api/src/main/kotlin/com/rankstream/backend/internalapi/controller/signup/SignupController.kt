package com.rankstream.backend.internalapi.controller.signup

import com.rankstream.backend.auth.extensions.withPasswordEncrypt
import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import com.rankstream.backend.domain.company.dto.response.CompanyRegistrationResponse
import com.rankstream.backend.domain.signup.SignupService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/signup")
class SignupController(
    private val signupService: SignupService
) {
    @PostMapping("")
    fun signup(@RequestBody @Validated companyRegistrationRequest: CompanyRegistrationRequest): ResponseEntity<CompanyRegistrationResponse> {
        val signUpResult = signupService.signup(companyRegistrationRequest.withPasswordEncrypt())
        return ResponseEntity.created(URI.create("/companies/${signUpResult.companyIndex}"))
            .body(signUpResult)
    }
}
