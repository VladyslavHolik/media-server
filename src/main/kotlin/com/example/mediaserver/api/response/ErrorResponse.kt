package com.example.mediaserver.api.response

data class ErrorResponse(
    val message: String,
    val errors: List<String> = mutableListOf()
)
