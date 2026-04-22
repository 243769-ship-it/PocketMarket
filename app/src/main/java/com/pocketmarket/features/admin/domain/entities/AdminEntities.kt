package com.pocketmarket.features.admin.domain.entities

data class AdminSeller(
    val usuarioId: Int,
    val email: String,
    val nombreMarca: String,
    val rfc: String,
    val telefono: String,
    val direccion: String,
    val clabe: String,
    val ineFrenteUrl: String,
    val ineTraseraUrl: String,
    val comprobanteUrl: String,
    val estado: String
)

data class StatsNumeros(
    val totalUsuarios: Int,
    val totalVendedores: Int,
    val pedidosCompletados: Int,
    val pedidosPendientes: Int,
    val ingresosTotales: Double
)

data class RevenueMonth(val label: String, val value: Double)
data class UserGrowth(val role: String, val count: Int)
data class TopProduct(val nombre: String, val precio: Double)

data class StatsGraficas(
    val totalRevenue: Double,
    val activeUsers: Int,
    val revenueByMonth: List<RevenueMonth>,
    val userGrowth: List<UserGrowth>,
    val topSellingProducts: List<TopProduct>
)

data class AdminStats(
    val numerosPrincipales: StatsNumeros,
    val statsGraficas: StatsGraficas
)