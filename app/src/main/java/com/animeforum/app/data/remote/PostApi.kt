package com.animeforum.app.data.remote
import com.animeforum.app.data.remote.dto.PostDto
import retrofit2.http.*

interface PostApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @POST("posts")
    suspend fun createPost(@Body post: PostDto): PostDto

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: PostDto
    ): PostDto

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}