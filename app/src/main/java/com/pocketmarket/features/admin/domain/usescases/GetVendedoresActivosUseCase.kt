package com.pocketmarket.features.admin.domain.usescases

import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetVendedoresActivosUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<List<AdminSeller>> {
        return repository.getVendedoresActivos()
    }
}