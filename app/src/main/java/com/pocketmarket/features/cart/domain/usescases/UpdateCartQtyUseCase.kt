package com.pocketmarket.features.cart.domain.usescases

import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class UpdateCartQtyUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: Int, cantidad: Int): Result<Unit> {
        return try {
            if (cantidad < 1) {
                Result.failure(Exception("La cantidad no puede ser menor a 1"))
            } else {
                Result.success(repository.actualizarCantidad(itemId, cantidad))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}