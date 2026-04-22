package com.pocketmarket.features.products.domain.usescases

import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    operator fun invoke(): Flow<List<Int>> {
        return repository.getFavoriteIds()
    }
}