package com.example.quizaro.network

// ==== Requests ====
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class ResetRequest(
    val username: String,
    val email: String
)

data class ResetConfirmRequest(
    val new_password: String,
    val confirm_password: String
)

// ==== Responses ====
data class User(
    val username: String,
    val email: String
)

data class UserResponse(
    val token: String,
    val user: User
)