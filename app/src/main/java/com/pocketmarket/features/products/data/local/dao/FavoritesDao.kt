package com.pocketmarket.features.products.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pocketmarket.features.products.data.local.entities.FavoriteListEntity
import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity
import com.pocketmarket.features.products.data.local.entities.ListWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(list: FavoriteListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: FavoriteProductEntity)

    @Transaction
    @Query("SELECT * FROM favorite_lists")
    fun getListsWithProducts(): Flow<List<ListWithProducts>>

    @Query("DELETE FROM favorite_products WHERE productId = :productId")
    fun deleteProductFromFavorites(productId: Int)
}