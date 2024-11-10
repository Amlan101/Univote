package com.example.univote.network

import com.example.univote.models.CreatePollRequest
import com.example.univote.models.Poll
import com.example.univote.models.PollDetailsResponse
import com.example.univote.models.ProtectedDataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("polls/{poll_id}")
    fun getPollDetails(@Path("poll_id") pollId: String): Call<PollDetailsResponse>

    @POST("polls")
    fun createPoll(@Body request: CreatePollRequest): Call<Void>

    @POST("vote")
    @FormUrlEncoded
    fun castVote(
        @Field("poll_id") pollId: String,
        @Field("option_id") optionId: String
    ): Call<Void>


    // Endpoint to deactivate a poll
    @PUT("polls/{poll_id}/deactivate")
    fun deactivatePoll(@Path("poll_id") pollId: String): Call<Void>

    // Endpoint to delete a poll
    @DELETE("polls/{poll_id}")
    fun deletePoll(@Path("poll_id") pollId: String): Call<Void>

}