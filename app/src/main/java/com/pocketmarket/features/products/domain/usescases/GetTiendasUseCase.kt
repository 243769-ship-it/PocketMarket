package com.pocketmarket.features.products.domain.usescases

import com.pocketmarket.features.products.domain.entities.Tienda
import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import javax.inject.Inject

class GetTiendasUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(): Result<List<Tienda>> {
        return try {
            Result.success(repository.getTiendas())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}