package com.pocketmarket.features.admin.data.datasources.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AdminPerfilDto(
    val nombreMarca: String,
    val rfc: String? = null,
    val clabeInterbancaria: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val ineFrenteUrl: String? = null,
    val ineTraseraUrl: String? = null,
    val comprobanteDomUrl: String? = null,
    val estado: String
)

@Serializable
data class AdminSellerDto(
    val id: Int,
    val email: String,
    val perfilVendedor: AdminPerfilDto?
)

@Serializable
data class AdminUpdateStateRequest(
    val estado: String,
    val mensajeRechazo: String? = null
)

@Serializable
data class StatsNumerosDto(
    val totalUsuarios: Int,
    val totalVendedores: Int,
    val pedidosCompletados: Int,
    val pedidosPendientes: Int,
    val ingresosTotales: Double
)

@Serializable
data class RevenueMonthDto(val label: String, val value: Double)

@Serializable
data class UserGrowthDto(val role: String, val count: Int)

@Serializable
data class TopProductDto(val nombre: String, val precio: Double)

@Serializable
data class StatsGraficasDto(
    val totalRevenue: Double,
    val activeUsers: Int,
    val revenueByMonth: List<RevenueMonthDto>,
    val userGrowth: List<UserGrowthDto>,
    val topSellingProducts: List<TopProductDto>
)

@Serializable
data class AdminStatsDto(
    val numerosPrincipales: StatsNumerosDto,
    val statsGraficas: StatsGraficasDto
)