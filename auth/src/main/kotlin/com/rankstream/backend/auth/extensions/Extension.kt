package com.rankstream.backend.auth.extensions

import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

val passwordEncoder = BCryptPasswordEncoder()

fun CompanyRegistrationRequest.withPasswordEncrypt(): CompanyRegistrationRequest {
    return this.copy(password = passwordEncoder.encode(this.password), passwordConfirm = passwordEncoder.encode(this.passwordConfirm))
}
