package com.pocketmarket.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pocketmarket.features.products.data.local.dao.FavoritesDao
import com.pocketmarket.features.products.data.local.entities.FavoriteListEntity
import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity

@Database(
    entities = [FavoriteListEntity::class, FavoriteProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PocketDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}