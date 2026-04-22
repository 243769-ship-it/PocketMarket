package com.pocketmarket.features.admin.data.repositories

import com.pocketmarket.features.admin.data.datasources.remote.api.AdminApi
import com.pocketmarket.features.admin.data.datasources.remote.mapper.toDomain
import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.domain.entities.AdminStats
import com.pocketmarket.features.admin.domain.repositories.AdminRepository
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val api: AdminApi
) : AdminRepository {

    override suspend fun getVendedoresPendientes(): Result<List<AdminSeller>> {
        return try {
            val dtos = api.getVendedoresPendientes()
            val sellers = dtos.filter { it.perfilVendedor != null }.map { it.toDomain() }
            Result.success(sellers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVendedoresActivos(): Result<List<AdminSeller>> {
        return try {
            val dtos = api.getVendedoresActivos()
            val sellers = dtos.filter { it.perfilVendedor != null }.map { it.toDomain() }
            Result.success(sellers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun aprobarVendedor(usuarioId: Int): Result<Unit> {
        return try {
            api.aprobarVendedor(usuarioId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun rechazarVendedor(usuarioId: Int, mensaje: String): Result<Unit> {
        return try {
            api.rechazarVendedor(usuarioId, mensaje)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStats(): Result<AdminStats> {
        return try {
            val dto = api.getStats()
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}