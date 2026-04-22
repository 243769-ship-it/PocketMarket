package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class BorrarProductoUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return try {
            Result.success(repository.borrarProducto(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}