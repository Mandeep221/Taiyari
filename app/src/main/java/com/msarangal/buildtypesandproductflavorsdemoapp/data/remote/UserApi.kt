package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("/users")
    suspend fun getUsers(): List<User>

    @GET("/posts")
    suspend fun getPostsByUser(@Query("userId") userId: String): List<Post>

    @GET("/todos/")
    suspend fun getTodos(): List<User>
    companion object {
        val retrofitApi: UserApi = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}

// BaseUrl = https://jsonplaceholder.typicode.com