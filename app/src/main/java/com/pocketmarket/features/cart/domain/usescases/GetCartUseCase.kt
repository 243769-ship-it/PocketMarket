package com.pocketmarket.features.cart.domain.usescases

import com.pocketmarket.features.cart.domain.entities.CartItem
import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(): Result<List<CartItem>> {
        return try {
            Result.success(repository.getCarrito())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}