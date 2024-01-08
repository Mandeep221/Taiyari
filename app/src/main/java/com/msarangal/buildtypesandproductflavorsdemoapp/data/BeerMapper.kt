package com.msarangal.buildtypesandproductflavorsdemoapp.data

import com.msarangal.buildtypesandproductflavorsdemoapp.data.local.BeerEntity
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Beer

fun Beer.toBeerEntity(): BeerEntity {
    return BeerEntity(
        id = id,
        name = name,
        tagline = tagline,
        description = description,
        firstBrewed = first_brewed,
        imageUrl = image_url
    )
}

fun BeerEntity.toBeer(): Beer {
    return Beer(
        id = id,
        name = name,
        tagline = tagline,
        description = description,
        first_brewed = firstBrewed,
        image_url = imageUrl
    )
}