package com.pocketmarket.features.cart.data.datasources.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class CarritoResponse(
    val items: List<ItemCarritoResponse> = emptyList()
)

@Serializable
data class ItemCarritoResponse(
    val id: Int,
    val cantidad: Int,
    val producto: ProductoCarritoResponse
)

@Serializable
data class ProductoCarritoResponse(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val fotoUrl: String
)

@Serializable
data class AddToCartRequest(
    val productoId: Int,
    val cantidad: Int
)

@Serializable
data class UpdateQtyRequest(
    val itemId: Int,
    val cantidad: Int
)