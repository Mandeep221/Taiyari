package com.msarangal.buildtypesandproductflavorsdemoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msarangal.buildtypesandproductflavorsdemoapp.data.MainRepository
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel() {

    private var _uiStateFlow = MutableStateFlow<UserUiState>(UserUiState.Loading)
    var uiStateFlow = _uiStateFlow.asStateFlow()

    private var _uiStateFlowPosts = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiStateFlowPosts = _uiStateFlowPosts.asStateFlow()

    private val _snackBarSharedFlow = MutableSharedFlow<Boolean>()
    var snackBarSharedFlow = _snackBarSharedFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            updateStateFlow(UserUiState.Loading)
            repository.fetchData()
                .flowOn(Dispatchers.IO)
                .catch {
                    updateStateFlow(UserUiState.Failure(it.message.toString()))
                }
                .collectLatest { list ->
                    delay(2000)
                    updateStateFlow(UserUiState.Success(list))
                    _snackBarSharedFlow.emit(true)
                }
        }
    }

    private fun updateStateFlow(uiState: UserUiState) {
        _uiStateFlow.value = uiState
    }

    private fun updatePostsUiState(uiStatePosts: PostUiState) {
        _uiStateFlowPosts.value = uiStatePosts
    }

    fun fetchPostsByUser(userId: String) {
        updatePostsUiState(PostUiState.Loading)
        viewModelScope.launch {
            repository.fetchPostsByUser(userId = userId)
                .flowOn(Dispatchers.IO)
                .catch {
                    updatePostsUiState(PostUiState.Failure(it.toString()))
                }.collectLatest { posts ->
                    delay(2000)
                    updatePostsUiState(PostUiState.Success(posts))
                }
        }
    }
}

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Failure(val errorMessage: String) : UserUiState()
}

sealed class PostUiState {
    object Loading : PostUiState()
    data class Success(val posts: List<Post>) : PostUiState()
    data class Failure(val errorMessage: String) : PostUiState()
}