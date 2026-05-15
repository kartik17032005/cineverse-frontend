package com.example.cineversemovieapp.models

//this represents the json sent to the springboot
data class RegisterRequest (
    val userName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)