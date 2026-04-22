package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class ActualizarEstadoPedidoUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(pedidoId: Int, estado: String): Result<Unit> {
        return try {
            Result.success(repository.actualizarEstadoPedido(pedidoId, estado))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}