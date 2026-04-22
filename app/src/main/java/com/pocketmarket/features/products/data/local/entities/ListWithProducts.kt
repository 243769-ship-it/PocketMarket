package com.pocketmarket.features.products.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithProducts(
    @Embedded val list: FavoriteListEntity,
    @Relation(
        parentColumn = "listId",
        entityColumn = "listId"
    )
    val products: List<FavoriteProductEntity>
)