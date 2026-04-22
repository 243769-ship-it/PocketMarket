package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.entities.SellerOrder
import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class GetPedidosUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(): Result<List<SellerOrder>> {
        return try {
            Result.success(repository.getPedidos())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}