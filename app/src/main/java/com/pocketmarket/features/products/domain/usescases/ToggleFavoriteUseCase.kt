package com.pocketmarket.features.products.domain.usescases

import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.repositories.ProductsRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(producto: Producto, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            repository.removeFavorite(producto.id)
        } else {
            repository.addFavorite(producto)
        }
    }
}