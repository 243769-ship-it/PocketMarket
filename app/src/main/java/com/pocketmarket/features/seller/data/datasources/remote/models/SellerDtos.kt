package com.pocketmarket.features.seller.data.datasources.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class SellerProductDto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val fotoUrl: String
)

@Serializable
data class SellerDashboardResponse(
    val totalVentas: Double,
    val pedidosPendientes: Int,
    val productosActivos: Int
)

@Serializable
data class ClienteDto(val email: String)

@Serializable
data class OrderItemProductoDto(val nombre: String, val fotoUrl: String)

@Serializable
data class SellerOrderItemDto(val cantidad: Int, val precio: Double, val producto: OrderItemProductoDto)

@Serializable
data class SellerOrderDto(
    val id: Int,
    val total: Double,
    val estado: String,
    val cliente: ClienteDto,
    val items: List<SellerOrderItemDto>
)

@Serializable
data class UpdateOrderStateRequest(val estado: String)