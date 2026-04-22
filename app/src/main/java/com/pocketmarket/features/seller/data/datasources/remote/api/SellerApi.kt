package com.pocketmarket.features.seller.data.datasources.remote.api

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.seller.data.datasources.remote.models.SellerDashboardResponse
import com.pocketmarket.features.seller.data.datasources.remote.models.SellerOrderDto
import com.pocketmarket.features.seller.data.datasources.remote.models.SellerProductDto
import com.pocketmarket.features.seller.data.datasources.remote.models.UpdateOrderStateRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import javax.inject.Inject

class SellerApi @Inject constructor(
    @PocketMarketKtorClient private val client: HttpClient
) {
    suspend fun getDashboardStats(): SellerDashboardResponse {
        val response = client.get("pedidos/vendedor/stats")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al obtener stats")
    }

    suspend fun getMisProductos(): List<SellerProductDto> {
        val response = client.get("productos/mis-productos")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al obtener productos")
    }

    suspend fun crearProducto(nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray) {
        val response = client.submitFormWithBinaryData(
            url = "productos/crear",
            formData = formData {
                append("nombre", nombre)
                append("descripcion", desc)
                append("precio", precio.toString())
                append("stock", stock.toString())
                append("foto", imagenBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"producto.jpg\"")
                })
            }
        )
        if (!response.status.isSuccess()) throw Exception("Error al crear")
    }

    suspend fun getPedidos(): List<SellerOrderDto> {
        val response = client.get("pedidos/vendedor")
        if (response.status.isSuccess()) return response.body()
        else throw Exception("Error al obtener pedidos")
    }

    suspend fun actualizarEstadoPedido(pedidoId: Int, estado: String) {
        val response = client.put("pedidos/vendedor/$pedidoId/estado") {
            contentType(ContentType.Application.Json)
            setBody(UpdateOrderStateRequest(estado))
        }
        if (!response.status.isSuccess()) throw Exception("Error al actualizar")
    }

    suspend fun borrarProducto(id: Int) {
        val response = client.delete("productos/$id")
        if (!response.status.isSuccess()) throw Exception("Error al borrar")
    }

    suspend fun editarProducto(id: Int, nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray?) {
        val response = client.put("productos/$id") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("nombre", nombre)
                        append("descripcion", desc)
                        append("precio", precio.toString())
                        append("stock", stock.toString())
                        if (imagenBytes != null) {
                            append("foto", imagenBytes, Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"editado.jpg\"")
                            })
                        }
                    }
                )
            )
        }
        if (!response.status.isSuccess()) throw Exception("Error al editar")
    }
}