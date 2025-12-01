package com.animeforum.app.data.remote.dto

data class PostDto(
    val id: Int? = null,
    val username: String,
    val animeTitle: String,
    val message: String,
    val category: String,
    val rating: Int,
    val timestamp: Long? = null
)
