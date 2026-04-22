package com.pocketmarket.features.cart.domain.usescases

import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            Result.success(repository.confirmarPedido())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}