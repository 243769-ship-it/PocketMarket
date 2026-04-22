package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class CrearProductoUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray): Result<Unit> {
        return try {
            Result.success(repository.crearProducto(nombre, desc, precio, stock, imagenBytes))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}