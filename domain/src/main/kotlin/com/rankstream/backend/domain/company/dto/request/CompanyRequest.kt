package com.rankstream.backend.domain.company.dto.request

import com.rankstream.backend.domain.company.dto.validator.CompanyRegistrationConstraint
import com.rankstream.backend.domain.company.enums.BusinessType
import com.rankstream.backend.domain.company.enums.CommissionPlan
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@CompanyRegistrationConstraint
data class CompanyRegistrationRequest(

    @field:NotBlank(message = "Email must not be blank")
    @field:Pattern(
        regexp = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$",
        message = "Email format is invalid"
    )
    @field:Size(min = 10, max = 50, message = "Email must be between 20 and 50 characters")
    val email: String,

    @field:NotBlank(message = "Password must not be blank")
    val password: String,

    @field:NotBlank(message = "Password must not be blank")
    val passwordConfirm: String,

    @field:NotBlank(message = "Company Name must not be blank")
    val businessLicense: String,

    @field:NotNull(message = "Business Type must not be null")
    val businessType: BusinessType,

    val representative: String?,

    val name: String?,

    val phoneNumber: String?,

    val postalCode: String?,

    val address: String?,

    val addressDetail: String?,

    val companyPostalCode: String?,

    val companyName: String?,

    val companyAddress: String?,

    val companyAddressDetail: String?,

    val companyPhone: String?,

)

data class CompanyCommissionRequest(
    @field:NotNull(message = "Commission Plan must not be null")
    val commissionPlan: CommissionPlan
)
