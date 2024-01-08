package com.msarangal.buildtypesandproductflavorsdemoapp.data.remote

import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Beer

interface BeerApiHelper {

    suspend fun getBeers(page: Int, perPage: Int): List<Beer>
}