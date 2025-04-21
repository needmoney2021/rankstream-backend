package com.rankstream.backend.domain.member.dto.validator

import com.rankstream.backend.domain.member.dto.request.MemberRegistrationRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MemberRegistrationValidator : ConstraintValidator<MemberRegistrationConstraint, MemberRegistrationRequest> {

    override fun isValid(
        value: MemberRegistrationRequest?,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) return false

        var valid = true
        context.disableDefaultConstraintViolation()

        if (value.id.isBlank()) {
            context.buildConstraintViolationWithTemplate("ID is required.")
                .addPropertyNode("id")
                .addConstraintViolation()
            valid = false
        } else if (value.id.length < 5 || value.id.length > 50) {
            context.buildConstraintViolationWithTemplate("Id must be between 5 and 50 characters")
                .addPropertyNode("id")
                .addConstraintViolation()
            valid = false
        }

        if (value.name.isBlank()) {
            context.buildConstraintViolationWithTemplate("Name is required.")
                .addPropertyNode("name")
                .addConstraintViolation()
            valid = false
        } else if (value.name.length < 2 || value.name.length > 50) {
            context.buildConstraintViolationWithTemplate("Name must be between 5 and 50 characters")
                .addPropertyNode("name")
                .addConstraintViolation()
            valid = false
        }

        if (value.gender == null) {
            context.buildConstraintViolationWithTemplate("Gender is required.")
                .addPropertyNode("gender")
                .addConstraintViolation()
            valid = false
        }

        if (!value.isGenesis) {
            if (value.recommenderId.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Recommender ID is required.")
                    .addPropertyNode("recommenderId")
                    .addConstraintViolation()
                valid = false
            }
            if (value.sponsorId.isNullOrBlank()) {
                context.buildConstraintViolationWithTemplate("Sponsor ID is required.")
                    .addPropertyNode("sponsorId")
                    .addConstraintViolation()
                valid = false
            }
            if (value.position == null) {
                context.buildConstraintViolationWithTemplate("Position is required.")
                    .addPropertyNode("position")
                    .addConstraintViolation()
                valid = false
            }
        }


        return valid
    }
}
