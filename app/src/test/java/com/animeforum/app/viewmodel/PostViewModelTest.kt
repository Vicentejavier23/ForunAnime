package com.animeforum.app.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.animeforum.app.data.model.Post
import com.animeforum.app.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: PostRepository
    private lateinit var viewModel: PostViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mock()
        whenever(repository.allPosts).thenReturn(flowOf(emptyList()))
        viewModel = PostViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `crear post válido debe llamar a insertPost en el repositorio`() = runTest {
        viewModel.updateUsername("NarutoUser")
        viewModel.updateAnimeTitle("Naruto")
        viewModel.updateMessage("Este es un mensaje válido para el post")
        viewModel.updateCategory("Shonen")
        viewModel.updateRating(5)

        var onSuccessCalled = false

        viewModel.validateAndSubmitPost { onSuccessCalled = true }
        dispatcher.scheduler.advanceUntilIdle()

        verify(repository).insertPost(any())
        assert(onSuccessCalled)
    }

    @Test
    fun `crear post inválido NO debe llamar a insertPost`() = runTest {
        viewModel.updateUsername("")
        viewModel.updateAnimeTitle("Naruto")
        viewModel.updateMessage("mensaje")
        viewModel.updateCategory("Shonen")
        viewModel.updateRating(5)

        viewModel.validateAndSubmitPost { }
        dispatcher.scheduler.advanceUntilIdle()

        verify(repository, never()).insertPost(any())
    }

    @Test
    fun `eliminar post debe llamar a deletePost en el repositorio`() = runTest {
        val postId = 42

        viewModel.deletePost(postId)
        dispatcher.scheduler.advanceUntilIdle()

        verify(repository).deletePostById(postId)
    }
}
