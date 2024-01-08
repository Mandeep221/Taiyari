package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Beer

class BeerApiHelperImpl(private val beerApi: BeerApi) : BeerApiHelper {
    override suspend fun getBeers(page: Int, perPage: Int): List<Beer> =
        beerApi.getBeers(page = page, perPage = perPage)
}