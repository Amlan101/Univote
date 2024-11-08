package com.example.univote.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Data classes for request and response payloads
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)


interface ApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}