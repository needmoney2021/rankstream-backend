package com.rankstream.backend.domain.auth.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
)
