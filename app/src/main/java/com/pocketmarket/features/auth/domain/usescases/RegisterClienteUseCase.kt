package com.pocketmarket.features.auth.domain.usecases

import com.pocketmarket.features.auth.domain.entities.Usuario
import com.pocketmarket.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterClienteUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Usuario> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                Result.failure(Exception("El correo y la contraseña son obligatorios"))
            } else {
                val usuario = repository.registrarCliente(email, password)
                Result.success(usuario)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}