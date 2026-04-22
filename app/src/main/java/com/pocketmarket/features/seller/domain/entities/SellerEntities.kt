package com.pocketmarket.features.seller.domain.entities

data class SellerProduct(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val fotoUrl: String
)

data class SellerDashboardStats(
    val totalVentas: Double,
    val pedidosPendientes: Int,
    val productosActivos: Int
)

data class SellerOrder(
    val id: Int,
    val total: Double,
    val estado: String,
    val clienteEmail: String,
    val items: List<SellerOrderItem>
)

data class SellerOrderItem(
    val nombreProducto: String,
    val fotoUrl: String,
    val cantidad: Int,
    val precio: Double
)