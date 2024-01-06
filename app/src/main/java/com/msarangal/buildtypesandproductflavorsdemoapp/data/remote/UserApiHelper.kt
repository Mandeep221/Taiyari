package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import kotlinx.coroutines.flow.Flow

interface UserApiHelper {
    fun getUsers(): Flow<List<User>>
    fun getPostsByUser(userId: String): Flow<List<Post>>
}