package com.rankstream.backend.domain.company.dto.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CompanyRegistrationValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CompanyRegistrationConstraint(
    val message: String = "Invalid company registration request",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

