package com.msarangal.buildtypesandproductflavorsdemoapp.di

import com.msarangal.buildtypesandproductflavorsdemoapp.data.MainRepository
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.UserApi
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.UserApiHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getRetrofit(): UserApi = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    @Singleton
    @Provides
    fun getMainRepo(api: UserApi): MainRepository = MainRepository(
        UserApiHelperImpl(userApi = api)
    )
}