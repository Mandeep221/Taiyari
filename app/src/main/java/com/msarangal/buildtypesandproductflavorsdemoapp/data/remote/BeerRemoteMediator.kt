package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerDatabase
import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerEntity
import com.msarangal.buildtypesandproductflavorsdemoapp.data.toBeerEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDb: BeerDatabase,
    private val beerApiHelper: BeerApiHelper
) : RemoteMediator<Int, BeerEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            delay(3000)
            val beers = beerApiHelper.getBeers(
                page = loadKey,
                perPage = state.config.pageSize
            )

            beerDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    beerDb.beerDao.clearAll()
                }
                val beerEntities = beers.map { it.toBeerEntity() }
                beerDb.beerDao.upsertAll(beerEntities)
            }
            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (httpException: HttpException) {
            MediatorResult.Error(httpException)
        }
    }
}