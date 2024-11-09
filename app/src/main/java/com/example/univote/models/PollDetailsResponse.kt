package com.example.univote.models

data class PollDetailsResponse(
    val id: String,
    val title: String,
    val options: List<Option>
)
