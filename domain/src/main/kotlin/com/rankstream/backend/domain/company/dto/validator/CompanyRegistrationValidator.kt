package com.rankstream.backend.domain.company.dto.validator

import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import com.rankstream.backend.domain.company.enums.BusinessType
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CompanyRegistrationValidator :
    ConstraintValidator<CompanyRegistrationConstraint, CompanyRegistrationRequest> {

    override fun isValid(value: CompanyRegistrationRequest?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false

        var valid = true
        context.disableDefaultConstraintViolation()

        // password == passwordConfirm 검증
        if (value.password != value.passwordConfirm) {
            context.buildConstraintViolationWithTemplate("Passwords do not match.")
                .addPropertyNode("passwordConfirm")
                .addConstraintViolation()
            valid = false
        }

        // 개인사업자 검증
        if (value.businessType == BusinessType.INDIVIDUAL) {
            if (value.name.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Name is required for individual businesses.")
                    .addPropertyNode("name")
                    .addConstraintViolation()
                valid = false
            }
        } else {
            // 법인사업자 검증
            if (value.companyPostalCode.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Company postal code is required.")
                    .addPropertyNode("companyPostalCode")
                    .addConstraintViolation()
                valid = false
            }
            if (value.companyAddress.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Company address is required.")
                    .addPropertyNode("companyAddress")
                    .addConstraintViolation()
                valid = false
            }
            if (value.companyAddressDetail.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Company address detail is required.")
                    .addPropertyNode("companyAddressDetail")
                    .addConstraintViolation()
                valid = false
            }
            if (value.representative.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Representative is required.")
                    .addPropertyNode("representative")
                    .addConstraintViolation()
                valid = false
            }
        }

        return valid
    }
}

