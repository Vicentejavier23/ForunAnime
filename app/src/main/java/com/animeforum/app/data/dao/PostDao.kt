package com.animeforum.app.data.dao
import androidx.room.*
import com.animeforum.app.data.model.Post
import kotlinx.coroutines.flow.Flow
@Dao interface PostDao{ @Query("SELECT * FROM posts ORDER BY timestamp DESC") fun getAllPosts():Flow<List<Post>>; @Query("SELECT * FROM posts WHERE id=:postId") suspend fun getPostById(postId:Int):Post?; @Query("SELECT * FROM posts WHERE category=:category ORDER BY timestamp DESC") fun getPostsByCategory(category:String):Flow<List<Post>>; @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun insertPost(post:Post):Long; @Update suspend fun updatePost(post:Post); @Delete suspend fun deletePost(post:Post); @Query("DELETE FROM posts WHERE id=:postId") suspend fun deletePostById(postId:Int) }