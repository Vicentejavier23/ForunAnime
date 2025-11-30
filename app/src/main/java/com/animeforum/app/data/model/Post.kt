package com.animeforum.app.data.model
import androidx.room.*
@Entity(tableName="posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val animeTitle: String,
    val message: String,
    val category: String,
    val rating: Int,
    val imagePaths: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
