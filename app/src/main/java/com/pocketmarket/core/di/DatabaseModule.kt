package com.pocketmarket.core.di

import android.content.Context
import androidx.room.Room
import com.pocketmarket.core.database.PocketDatabase
import com.pocketmarket.features.products.data.local.dao.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePocketDatabase(@ApplicationContext context: Context): PocketDatabase {
        return Room.databaseBuilder(
            context,
            PocketDatabase::class.java,
            "pocket_market_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoritesDao(database: PocketDatabase): FavoritesDao {
        return database.favoritesDao()
    }
}