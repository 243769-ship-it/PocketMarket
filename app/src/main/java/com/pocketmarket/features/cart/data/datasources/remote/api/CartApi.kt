package com.pocketmarket.features.cart.data.datasources.remote.api

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.cart.data.datasources.remote.models.AddToCartRequest
import com.pocketmarket.features.cart.data.datasources.remote.models.CarritoResponse
import com.pocketmarket.features.cart.data.datasources.remote.models.UpdateQtyRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class CartApi @Inject constructor(
    @PocketMarketKtorClient private val client: HttpClient
) {
    suspend fun getCarrito(): CarritoResponse {
        val response = client.get("carrito")
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Error al obtener carrito: ${response.status.value}")
        }
    }

    suspend fun agregarAlCarrito(request: AddToCartRequest) {
        val response = client.post("carrito/agregar") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Error al agregar al carrito")
        }
    }

    suspend fun actualizarCantidad(request: UpdateQtyRequest) {
        val response = client.put("carrito/actualizar") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Error al actualizar cantidad")
        }
    }

    suspend fun eliminarItem(itemId: Int) {
        val response = client.delete("carrito/$itemId")
        if (!response.status.isSuccess()) {
            throw Exception("Error al eliminar item")
        }
    }

    suspend fun confirmarPedido() {
        val response = client.post("pedidos/checkout")
        if (!response.status.isSuccess()) {
            throw Exception("Error al confirmar pedido")
        }
    }
}