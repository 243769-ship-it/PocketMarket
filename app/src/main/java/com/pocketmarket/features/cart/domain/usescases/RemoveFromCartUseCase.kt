package com.pocketmarket.features.cart.domain.usescases

import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: Int): Result<Unit> {
        return try {
            Result.success(repository.eliminarItem(itemId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
