package com.pocketmarket.features.seller.domain.usescases

import com.pocketmarket.features.seller.domain.entities.SellerDashboardStats
import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class GetDashboardStatsUseCase @Inject constructor(
    private val repository: SellerRepository
) {
    suspend operator fun invoke(): Result<SellerDashboardStats> {
        return try {
            Result.success(repository.getDashboardStats())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}