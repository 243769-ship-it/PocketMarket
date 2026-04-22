package com.pocketmarket.features.products.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_products",
    foreignKeys = [
        ForeignKey(
            entity = FavoriteListEntity::class,
            parentColumns = ["listId"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteProductEntity(
    @PrimaryKey val productId: Int,
    val listId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)