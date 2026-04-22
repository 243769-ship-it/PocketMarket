package com.pocketmarket.features.cart.domain.usescases

import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(productoId: Int, cantidad: Int): Result<Unit> {
        return try {
            Result.success(repository.agregarAlCarrito(productoId, cantidad))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}