package com.pocketmarket.features.admin.domain.repositories

import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.domain.entities.AdminStats

interface AdminRepository {
    suspend fun getVendedoresPendientes(): Result<List<AdminSeller>>
    suspend fun getVendedoresActivos(): Result<List<AdminSeller>>
    suspend fun aprobarVendedor(usuarioId: Int): Result<Unit>
    suspend fun rechazarVendedor(usuarioId: Int, mensaje: String): Result<Unit>
    suspend fun getStats(): Result<AdminStats>
}