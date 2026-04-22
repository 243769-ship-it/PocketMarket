package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.entities.SellerProduct
import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class GetMisProductosUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(): Result<List<SellerProduct>> {
        return try {
            Result.success(repository.getMisProductos())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}