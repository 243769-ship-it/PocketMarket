package com.pocketmarket.features.admin.domain.usescases

import com.pocketmarket.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class AprobarVendedorUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(usuarioId: Int): Result<Unit> {
        return repository.aprobarVendedor(usuarioId)
    }
}