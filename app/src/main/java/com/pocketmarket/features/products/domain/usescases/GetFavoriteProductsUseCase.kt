package com.pocketmarket.features.products.domain.usescases

import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity
import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    operator fun invoke(): Flow<List<FavoriteProductEntity>> {
        return repository.getFavoriteProducts()
    }
}