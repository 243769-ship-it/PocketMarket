package com.pocketmarket.features.admin.data.datasources.remote.api

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.admin.data.datasources.remote.models.AdminSellerDto
import com.pocketmarket.features.admin.data.datasources.remote.models.AdminStatsDto
import com.pocketmarket.features.admin.data.datasources.remote.models.AdminUpdateStateRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class AdminApi @Inject constructor(
    @PocketMarketKtorClient private val client: HttpClient
) {
    suspend fun getVendedoresPendientes(): List<AdminSellerDto> {
        val response = client.get("admin/vendedores/pendientes")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al obtener solicitudes")
    }

    suspend fun getVendedoresActivos(): List<AdminSellerDto> {
        val response = client.get("admin/vendedores")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al obtener vendedores")
    }

    suspend fun aprobarVendedor(usuarioId: Int) {
        val response = client.put("admin/vendedores/$usuarioId/estado") {
            contentType(ContentType.Application.Json)
            setBody(AdminUpdateStateRequest("APROBADO"))
        }
        if (!response.status.isSuccess()) throw Exception("Error al aprobar")
    }

    suspend fun rechazarVendedor(usuarioId: Int, mensaje: String) {
        val response = client.put("admin/vendedores/$usuarioId/estado") {
            contentType(ContentType.Application.Json)
            setBody(AdminUpdateStateRequest("BLOQUEADO", mensaje))
        }
        if (!response.status.isSuccess()) throw Exception("Error al rechazar")
    }

    suspend fun getStats(): AdminStatsDto {
        val response = client.get("admin/stats")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al cargar estadísticas")
    }
}