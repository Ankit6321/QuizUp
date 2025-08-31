package com.example.quizaro.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // IMPORTANT:
    // Emulator talking to your PC's localhost server uses 10.0.2.2
    // Real phone on same Wi-Fi uses your PC's LAN IP (e.g., http://192.168.1.5:8000/)
    private const val BASE_URL = "http://35.226.116.92/api/users/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // MUST end with /
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
