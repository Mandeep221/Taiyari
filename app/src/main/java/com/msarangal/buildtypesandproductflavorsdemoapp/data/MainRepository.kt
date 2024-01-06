package com.msarangal.buildtypesandproductflavorsdemoapp.data

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.UserApiHelper
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val userApiHelper: UserApiHelper
) {
    fun fetchData(): Flow<List<User>> = userApiHelper.getUsers()

    fun fetchPostsByUser(userId: String): Flow<List<Post>> =
        userApiHelper.getPostsByUser(userId = userId)
}