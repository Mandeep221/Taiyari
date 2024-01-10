package com.msarangal.buildtypesandproductflavorsdemoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.msarangal.buildtypesandproductflavorsdemoapp.data.MainRepository
import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerEntity
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import com.msarangal.buildtypesandproductflavorsdemoapp.data.toBeer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    pager: Pager<Int, BeerEntity>
) : ViewModel() {

    private var _uiStateFlow = MutableStateFlow<UserUiState>(UserUiState.Loading)
    var uiStateFlow = _uiStateFlow.asStateFlow()

    private var _uiStateFlowPosts = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val uiStateFlowPosts = _uiStateFlowPosts.asStateFlow()

    private val _snackBarSharedFlow = MutableSharedFlow<Boolean>()
    var snackBarSharedFlow = _snackBarSharedFlow.asSharedFlow()

    val beerPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toBeer()
            }
        }.cachedIn(viewModelScope)

    init {
//        viewModelScope.launch {
//            updateStateFlow(UserUiState.Loading)
//            repository.fetchUsers()
//                .flowOn(Dispatchers.IO)
//                .catch {
//                    updateStateFlow(UserUiState.Failure(it.message.toString()))
//                }
//                .collectLatest { list ->
//                    delay(2000)
//                    updateStateFlow(UserUiState.Success(list))
//                    _snackBarSharedFlow.emit(true)
//                }
//        }
        fetchUserAndPostsInParallel()
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

    // Series Api call
    // fetch all users, get all posts by the 3rd user using userId
    fun fetchPostsWithSeriesApiCall() {
        updatePostsUiState(PostUiState.Loading)
        viewModelScope.launch {
            repository.fetchUsers()
                .flatMapLatest {
                    repository.fetchPostsByUser(it[6].id.toString())
                }.flowOn(Dispatchers.IO)
                .catch {
                    updatePostsUiState(PostUiState.Failure(it.toString()))
                }.collectLatest { posts ->
                    delay(2000)
                    updatePostsUiState(PostUiState.Success(posts))
                }

        }
    }

    // Parallel Api call
    // Fetch all Users and all the Posts simultaneously
    private fun fetchUserAndPostsInParallel() {
        updateStateFlow(UserUiState.Loading)
        updatePostsUiState(PostUiState.Loading)
        viewModelScope.launch {
            repository.fetchUsers()
                .zip(repository.fetchPostsByUser("3")) { users, posts ->
                    updateStateFlow(UserUiState.Success(users))
                    updatePostsUiState(PostUiState.Success(posts))
                }.flowOn(Dispatchers.IO)
                .catch {
                    updateStateFlow(UserUiState.Failure(it.message.toString()))
                    updatePostsUiState(PostUiState.Failure(it.message.toString()))
                }
                .collect()
        }
    }

    // Tomorrow, practice combine, zip and flatMap operators in Kotlin Flows

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

sealed class AllPostsUiState {
    object Loading : AllPostsUiState()
    data class Success(val posts: List<Post>) : AllPostsUiState()
    data class Failure(val errorMessage: String) : AllPostsUiState()
}