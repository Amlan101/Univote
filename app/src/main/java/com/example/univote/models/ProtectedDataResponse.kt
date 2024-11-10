package com.example.univote.models

import java.io.Serializable

data class ProtectedDataResponse(
    val pollId: String,
    val title: String,
    val options: List<Option>,
    val totalVotes: Int
): Serializable