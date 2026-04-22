package com.pocketmarket.features.cart.data.datasources.remote.mapper

import com.pocketmarket.features.cart.data.datasources.remote.models.ItemCarritoResponse
import com.pocketmarket.features.cart.domain.entities.CartItem

fun ItemCarritoResponse.toDomain(): CartItem {
    return CartItem(
        id = this.id,
        productoId = this.producto.id,
        nombre = this.producto.nombre,
        precio = this.producto.precio,
        cantidad = this.cantidad,
        fotoUrl = this.producto.fotoUrl
    )
}