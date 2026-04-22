package com.pocketmarket.features.products.domain.repositories

import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity
import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.entities.Tienda
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getCatalogo(search: String = ""): List<Producto>
    suspend fun getTiendas(): List<Tienda>
    fun getFavoriteIds(): Flow<List<Int>>
    fun getFavoriteProducts(): Flow<List<FavoriteProductEntity>>
    suspend fun addFavorite(producto: Producto)
    suspend fun removeFavorite(productId: Int)
}