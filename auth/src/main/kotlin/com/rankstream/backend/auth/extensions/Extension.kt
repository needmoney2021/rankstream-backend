package com.rankstream.backend.auth.extensions

import com.rankstream.backend.domain.admin.dto.request.AdministratorRegistrationCommand
import com.rankstream.backend.domain.admin.dto.request.AdministratorRegistrationRequest
import com.rankstream.backend.domain.admin.dto.request.AdministratorUpdateCommand
import com.rankstream.backend.domain.admin.dto.request.AdministratorUpdateRequest
import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

val passwordEncoder = BCryptPasswordEncoder()

fun CompanyRegistrationRequest.withPasswordEncrypt(): CompanyRegistrationRequest {
    return this.copy(password = passwordEncoder.encode(this.password), passwordConfirm = passwordEncoder.encode(this.passwordConfirm))
}


fun AdministratorUpdateRequest.withPasswordEncrypt(): AdministratorUpdateCommand {
    return AdministratorUpdateCommand(
        state = this.state,
        department = this.department,
        newEncodedPassword = this.newPassword?.let { passwordEncoder.encode(it) }
    )
}

fun AdministratorRegistrationRequest.withPasswordEncrypt(): AdministratorRegistrationCommand {
    return AdministratorRegistrationCommand(
        name = this.name,
        id = this.id,
        department = this.department,
        encodedPassword = passwordEncoder.encode(this.password)
    )
}
