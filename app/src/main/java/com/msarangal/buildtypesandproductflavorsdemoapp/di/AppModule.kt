package com.msarangal.buildtypesandproductflavorsdemoapp.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.msarangal.buildtypesandproductflavorsdemoapp.data.MainRepository
import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerDatabase
import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerEntity
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.BeerApi
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.BeerApiHelperImpl
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.BeerRemoteMediator
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.UserApi
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.UserApiHelperImpl
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Beer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getUserApi(client: OkHttpClient): UserApi = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    @Singleton
    @Provides
    fun providesOkhttpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun getBeerApi(client: OkHttpClient): BeerApi = Retrofit.Builder()
        .baseUrl("https://api.punkapi.com/v2/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BeerApi::class.java)

    @Singleton
    @Provides
    fun getMainRepo(api: UserApi, beerApi: BeerApi): MainRepository = MainRepository(
        UserApiHelperImpl(userApi = api),
        BeerApiHelperImpl(beerApi = beerApi)
    )

    @Singleton
    @Provides
    fun getBeerDatabase(@ApplicationContext app: Context): BeerDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = BeerDatabase::class.java,
            name = "beer_db"
        ).build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun getBeerPager(beerDb: BeerDatabase, beerApi: BeerApi): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 6),
            remoteMediator = BeerRemoteMediator(
                beerDb = beerDb,
                beerApiHelper = BeerApiHelperImpl(beerApi)
            ),
            pagingSourceFactory = {
                beerDb.beerDao.pagingSource()
            }
        )
    }
}