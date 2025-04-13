package com.rankstream.backend.internalapi.controller.csrf

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/csrf")
class CsrfController {

    @GetMapping("")
    fun getCsrfToken(request: HttpServletRequest): ResponseEntity<Void> {
        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken
            ?: return ResponseEntity.badRequest().build()

        csrfToken.token

        return ResponseEntity.noContent().build()
    }
}
