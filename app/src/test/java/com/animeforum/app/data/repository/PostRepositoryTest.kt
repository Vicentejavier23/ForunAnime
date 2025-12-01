package com.animeforum.app.data.repository

import com.animeforum.app.data.dao.PostDao
import com.animeforum.app.data.model.Post
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PostRepositoryTest {

    private lateinit var dao: PostDao
    private lateinit var repository: PostRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = PostRepository(dao)
    }



    @Test
    fun `insertPost delega en dao`() = runTest {
        val post = Post(
            id = 0,
            username = "user",
            animeTitle = "One Piece",
            message = "mensaje",
            category = "Shonen",
            rating = 4,
            imagePaths = emptyList(),
            timestamp = 0L
        )

        repository.insertPost(post)

        verify(dao).insertPost(post)
    }

    @Test
    fun `updatePost delega en dao`() = runTest {
        val post = Post(
            id = 2,
            username = "user",
            animeTitle = "Bleach",
            message = "mensaje",
            category = "Shonen",
            rating = 3,
            imagePaths = emptyList(),
            timestamp = 0L
        )

        repository.updatePost(post)

        verify(dao).updatePost(post)
    }

    @Test
    fun `deletePost delega en dao`() = runTest {
        val post = Post(
            id = 3,
            username = "user",
            animeTitle = "Gintama",
            message = "mensaje",
            category = "Comedia",
            rating = 5,
            imagePaths = emptyList(),
            timestamp = 0L
        )

        repository.deletePost(post)

        verify(dao).deletePost(post)
    }

    @Test
    fun `deletePostById delega en dao`() = runTest {
        val postId = 10

        repository.deletePostById(postId)

        verify(dao).deletePostById(postId)
    }

    @Test
    fun `getPostById delega en dao`() = runTest {
        val postId = 7

        repository.getPostById(postId)

        verify(dao).getPostById(postId)
    }

    @Test
    fun `getPostsByCategory delega en dao`() {
        val category = "Shonen"

        repository.getPostsByCategory(category)

        verify(dao).getPostsByCategory(category)
    }
}
