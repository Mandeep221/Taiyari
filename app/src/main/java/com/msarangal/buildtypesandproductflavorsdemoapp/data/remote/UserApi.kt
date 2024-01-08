package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("/users")
    suspend fun getUsers(): List<User>

    @GET("/posts")
    suspend fun getPostsByUser(@Query("userId") userId: String): List<Post>

    @GET("/todos/")
    suspend fun getTodos(): List<User>
}

// BaseUrl = https://jsonplaceholder.typicode.com