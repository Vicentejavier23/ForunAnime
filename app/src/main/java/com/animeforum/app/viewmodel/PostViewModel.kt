package com.animeforum.app.viewmodel
import androidx.lifecycle.*
import com.animeforum.app.data.dao.PostDao
import com.animeforum.app.data.model.Post
import com.animeforum.app.data.network.AnimeApiService
import com.animeforum.app.data.network.AnimeSuggestion
import com.animeforum.app.data.repository.PostRepository
import com.animeforum.app.utils.ValidationHelper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class PostViewModel(private val repo: PostRepository) : ViewModel() {
  val allPosts: StateFlow<List<Post>> = repo.allPosts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  private val _ui = MutableStateFlow(PostUiState()); val uiState: StateFlow<PostUiState> = _ui.asStateFlow()
  private val _cat = MutableStateFlow("Todos"); val selectedCategory: StateFlow<String> = _cat.asStateFlow()
  val filteredPosts: StateFlow<List<Post>> = combine(allPosts, selectedCategory) { p, c -> if (c == "Todos") p else p.filter { it.category == c } }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  private val animeApi = AnimeApiService()

  fun updateUsername(v: String) { _ui.update { it.copy(username = v, usernameError = null) } }
  fun updateAnimeTitle(v: String) { _ui.update { it.copy(animeTitle = v, animeTitleError = null) } }
  fun updateMessage(v: String) { _ui.update { it.copy(message = v, messageError = null) } }
  fun updateCategory(v: String) { _ui.update { it.copy(category = v, categoryError = null) } }
  fun updateRating(v: Int) { _ui.update { it.copy(rating = v, ratingError = null) } }
  fun setSelectedCategory(c: String) { _cat.value = c }

  fun loadPostForEdit(postId: Int?) {
    if (postId == null) {
      _ui.value = PostUiState()
      return
    }

    viewModelScope.launch {
      _ui.update { it.copy(isLoading = true) }
      val post = repo.getPostById(postId)
      if (post != null) {
        _ui.value = PostUiState(
          postId = post.id,
          username = post.username,
          animeTitle = post.animeTitle,
          message = post.message,
          category = post.category,
          rating = post.rating,
          imagePaths = post.imagePaths,
          timestamp = post.timestamp,
          isEditing = true
        )
      } else {
        _ui.value = PostUiState()
      }
      _ui.update { it.copy(isLoading = false) }
    }
  }

  fun addImagePath(path: String) { _ui.update { it.copy(imagePaths = it.imagePaths + path) } }

  fun removeImagePath(path: String) { _ui.update { it.copy(imagePaths = it.imagePaths - path) } }

  fun appendToUsername(text: String) { _ui.update { it.copy(username = (it.username + " " + text).trim()) } }

  fun appendToAnimeTitle(text: String) { _ui.update { it.copy(animeTitle = (it.animeTitle + " " + text).trim()) } }

  fun appendToMessage(text: String) { _ui.update { it.copy(message = (it.message + " " + text).trim()) } }

  fun fetchAnimeSuggestion() {
    viewModelScope.launch {
      _ui.update { it.copy(isFetchingSuggestion = true, suggestionError = null) }
      val suggestion: AnimeSuggestion? = animeApi.fetchRandomAnimeSuggestion()
      if (suggestion != null) {
        _ui.update {
          it.copy(
            animeTitle = suggestion.title,
            message = if (it.message.isBlank()) suggestion.synopsis.orEmpty() else it.message,
            suggestedAnimeTitle = suggestion.title,
            suggestedAnimeSynopsis = suggestion.synopsis,
            isFetchingSuggestion = false,
            suggestionError = null
          )
        }
      } else {
        _ui.update { it.copy(isFetchingSuggestion = false, suggestionError = "No se pudo obtener una sugerencia en este momento.") }
      }
    }
  }

  fun validateAndSubmitPost(onSuccess: () -> Unit) {
    val s = _ui.value; val u = ValidationHelper.validateUsername(s.username); val t = ValidationHelper.validateAnimeTitle(s.animeTitle); val m = ValidationHelper.validateMessage(s.message); val c = ValidationHelper.validateCategory(s.category); val r = ValidationHelper.validateRating(s.rating)
    _ui.update { it.copy(usernameError = u.errorMessage.takeIf { !u.isValid }, animeTitleError = t.errorMessage.takeIf { !t.isValid }, messageError = m.errorMessage.takeIf { !m.isValid }, categoryError = c.errorMessage.takeIf { !c.isValid }, ratingError = r.errorMessage.takeIf { !r.isValid }) }
    if (u.isValid && t.isValid && m.isValid && c.isValid && r.isValid) {
      viewModelScope.launch {
        _ui.update { it.copy(isLoading = true) }
        val post = Post(
          id = s.postId ?: 0,
          username = s.username,
          animeTitle = s.animeTitle,
          message = s.message,
          category = s.category,
          rating = s.rating,
          imagePaths = s.imagePaths,
          timestamp = if (s.isEditing) s.timestamp else System.currentTimeMillis()
        )
        if (s.isEditing) repo.updatePost(post) else repo.insertPost(post)
        _ui.value = PostUiState()
        onSuccess()
      }
    }
  }
  fun deletePost(id: Int) = viewModelScope.launch { repo.deletePostById(id) }
  fun clearForm() { _ui.value = PostUiState() }
}

data class PostUiState(
  val postId: Int? = null,
  val username: String = "",
  val animeTitle: String = "",
  val message: String = "",
  val category: String = "",
  val rating: Int = 3,
  val imagePaths: List<String> = emptyList(),
  val timestamp: Long = System.currentTimeMillis(),
  val isEditing: Boolean = false,
  val suggestedAnimeTitle: String? = null,
  val suggestedAnimeSynopsis: String? = null,
  val usernameError: String? = null,
  val animeTitleError: String? = null,
  val messageError: String? = null,
  val categoryError: String? = null,
  val ratingError: String? = null,
  val isLoading: Boolean = false,
  val isFetchingSuggestion: Boolean = false,
  val suggestionError: String? = null
)
class PostViewModelFactory(private val dao:PostDao): ViewModelProvider.Factory{ override fun <T: ViewModel> create(modelClass: Class<T>): T { @Suppress("UNCHECKED_CAST") return PostViewModel(PostRepository(dao)) as T } }
