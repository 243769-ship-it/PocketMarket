package com.pocketmarket.features.admin.domain.usescases

import com.pocketmarket.features.admin.domain.entities.AdminStats
import com.pocketmarket.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(): Result<AdminStats> {
        return repository.getStats()
    }
}