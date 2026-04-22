package com.pocketmarket.features.auth.domain.usecases

import com.pocketmarket.features.auth.domain.entities.Usuario
import com.pocketmarket.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterSellerUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String, pass: String, marca: String, rfc: String,
        clabe: String, tel: String, dir: String,
        ineF: ByteArray, ineT: ByteArray, comp: ByteArray
    ): Result<Usuario> {
        return try {
            if (email.isBlank() || pass.isBlank() || marca.isBlank()) {
                Result.failure(Exception("Todos los campos son obligatorios"))
            } else {
                val usuario = repository.registrarVendedor(email, pass, marca, rfc, clabe, tel, dir, ineF, ineT, comp)
                Result.success(usuario)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}