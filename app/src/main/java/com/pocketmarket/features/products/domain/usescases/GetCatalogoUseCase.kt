package com.pocketmarket.features.products.domain.usescases

import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import javax.inject.Inject

class GetCatalogoUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(search: String = ""): Result<List<Producto>> {
        return try {
            Result.success(repository.getCatalogo(search))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}