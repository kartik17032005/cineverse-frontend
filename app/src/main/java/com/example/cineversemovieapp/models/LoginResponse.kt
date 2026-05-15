package com.example.cineversemovieapp.models

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val email: String
)
