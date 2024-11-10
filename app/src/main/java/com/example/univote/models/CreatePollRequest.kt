package com.example.univote.models

data class CreatePollRequest(
    val title: String,
    val options: List<String>
)
