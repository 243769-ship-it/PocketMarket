package com.pocketmarket.features.cart.domain.entities

data class CartItem(
    val id: Int,
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val fotoUrl: String
)