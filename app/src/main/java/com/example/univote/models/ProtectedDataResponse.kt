package com.example.univote.models

data class ProtectedDataResponse(
    val poll_id: String,
    val title: String,
    val options: List<Option>,
    val total_votes: Int
)

data class Option(
    val option: String,
    val votes: Int
)
