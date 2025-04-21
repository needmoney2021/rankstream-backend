package com.rankstream.backend.domain.member.dto.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload

@MustBeDocumented
@Constraint(validatedBy = [MemberRegistrationValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberRegistrationConstraint(
    val message: String = "Invalid member registration request",
    val groups: Array<kotlin.reflect.KClass<*>> = [],
    val payload: Array<kotlin.reflect.KClass<out Payload>> = []
)
