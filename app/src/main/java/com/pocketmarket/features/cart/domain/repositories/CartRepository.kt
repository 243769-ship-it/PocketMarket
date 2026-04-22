package com.pocketmarket.features.cart.domain.repositories

import com.pocketmarket.features.cart.domain.entities.CartItem

interface CartRepository {
    suspend fun getCarrito(): List<CartItem>
    suspend fun agregarAlCarrito(productoId: Int, cantidad: Int)
    suspend fun actualizarCantidad(itemId: Int, nuevaCantidad: Int)
    suspend fun eliminarItem(itemId: Int)
    suspend fun confirmarPedido()
}