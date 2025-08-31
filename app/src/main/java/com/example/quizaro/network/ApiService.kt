package com.example.quizaro.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Match your Django endpoints from the web tester
interface ApiService {

    @POST("register/")
    fun registerUser(@Body request: RegisterRequest): Call<UserResponse>

    @POST("login/")
    fun loginUser(@Body request: LoginRequest): Call<UserResponse>

    // Django expects: Authorization: Token <token>
    @POST("logout/")
    fun logoutUser(@Header("Authorization") tokenHeader: String): Call<Void>

    @POST("reset-password/")
    fun requestPasswordReset(@Body request: ResetRequest): Call<Map<String, Any>>

    @POST("reset-password-confirm/{uid}/{token}/")
    fun confirmPasswordReset(
        @Path("uid") uid: String,
        @Path("token") token: String,
        @Body request: ResetConfirmRequest
    ): Call<Map<String, Any>>
}
