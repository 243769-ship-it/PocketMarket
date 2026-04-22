package com.pocketmarket.features.products.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_lists")
data class FavoriteListEntity(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    val name: String
)