package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserApiHelperImpl(private val userApi: UserApi) : UserApiHelper {
    override fun getUsers(): Flow<List<User>> = flow { emit(userApi.getUsers()) }
    override fun getPostsByUser(userId: String): Flow<List<Post>> =
        flow { emit(userApi.getPostsByUser(userId = userId)) }
}