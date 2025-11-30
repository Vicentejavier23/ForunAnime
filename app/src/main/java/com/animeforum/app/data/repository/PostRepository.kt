package com.animeforum.app.data.repository
import com.animeforum.app.data.dao.PostDao
import com.animeforum.app.data.model.Post
import kotlinx.coroutines.flow.Flow
class PostRepository(private val dao:PostDao){ val allPosts:Flow<List<Post>> = dao.getAllPosts(); suspend fun getPostById(id:Int)=dao.getPostById(id); fun getPostsByCategory(c:String)=dao.getPostsByCategory(c); suspend fun insertPost(p:Post)=dao.insertPost(p); suspend fun updatePost(p:Post)=dao.updatePost(p); suspend fun deletePost(p:Post)=dao.deletePost(p); suspend fun deletePostById(id:Int)=dao.deletePostById(id) }