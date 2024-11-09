package com.example.univote.network

import com.example.univote.models.Poll
import com.example.univote.models.ProtectedDataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Data classes for request and response payloads
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)


interface ApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<Void>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("polls/{poll_id}/results")
    fun getPollResults(@Path("poll_id") pollId: String): Call<ProtectedDataResponse>

    @GET("polls")
    fun getActivePolls(): Call<List<Poll>>

}