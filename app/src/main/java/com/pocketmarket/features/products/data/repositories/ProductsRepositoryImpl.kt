package com.pocketmarket.features.products.data.repositories

import com.pocketmarket.features.products.data.datasources.remote.api.ProductsApi
import com.pocketmarket.features.products.data.datasources.remote.mapper.toDomain
import com.pocketmarket.features.products.data.local.dao.FavoritesDao
import com.pocketmarket.features.products.data.local.entities.FavoriteListEntity
import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity
import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.entities.Tienda
import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: ProductsApi,
    private val favoritesDao: FavoritesDao
) : ProductsRepository {

    override suspend fun getCatalogo(search: String): List<Producto> {
        val response = api.getCatalogo(search)
        return response.map { it.toDomain() }
    }

    override suspend fun getTiendas(): List<Tienda> {
        val response = api.getTiendas()
        return response.map { it.toDomain() }
    }

    override fun getFavoriteIds(): Flow<List<Int>> {
        return favoritesDao.getListsWithProducts().map { lists ->
            lists.firstOrNull()?.products?.map { it.productId } ?: emptyList()
        }
    }

    override fun getFavoriteProducts(): Flow<List<FavoriteProductEntity>> {
        return favoritesDao.getListsWithProducts().map { lists ->
            lists.firstOrNull()?.products ?: emptyList()
        }
    }

    override suspend fun addFavorite(producto: Producto) {
        withContext(Dispatchers.IO) {
            favoritesDao.insertList(FavoriteListEntity(listId = 1, name = "Mis Favoritos"))
            favoritesDao.insertProduct(
                FavoriteProductEntity(
                    productId = producto.id,
                    listId = 1,
                    name = producto.nombre,
                    price = producto.precio,
                    imageUrl = producto.fotoUrl
                )
            )
        }
    }

    override suspend fun removeFavorite(productId: Int) {
        withContext(Dispatchers.IO) {
            favoritesDao.deleteProductFromFavorites(productId)
        }
    }
}