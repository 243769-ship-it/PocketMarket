package com.pocketmarket.features.cart.data.repositories

import com.pocketmarket.features.cart.data.datasources.remote.api.CartApi
import com.pocketmarket.features.cart.data.datasources.remote.mapper.toDomain
import com.pocketmarket.features.cart.data.datasources.remote.models.AddToCartRequest
import com.pocketmarket.features.cart.data.datasources.remote.models.UpdateQtyRequest
import com.pocketmarket.features.cart.domain.entities.CartItem
import com.pocketmarket.features.cart.domain.repositories.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: CartApi
) : CartRepository {

    override suspend fun getCarrito(): List<CartItem> {
        val response = api.getCarrito()
        return response.items.map { it.toDomain() }
    }

    override suspend fun agregarAlCarrito(productoId: Int, cantidad: Int) {
        api.agregarAlCarrito(AddToCartRequest(productoId, cantidad))
    }

    override suspend fun actualizarCantidad(itemId: Int, nuevaCantidad: Int) {
        api.actualizarCantidad(UpdateQtyRequest(itemId, nuevaCantidad))
    }

    override suspend fun eliminarItem(itemId: Int) {
        api.eliminarItem(itemId)
    }

    override suspend fun confirmarPedido() {
        api.confirmarPedido()
    }
}