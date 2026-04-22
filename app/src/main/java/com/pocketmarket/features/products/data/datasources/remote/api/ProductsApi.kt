package com.pocketmarket.features.products.data.datasources.remote.api

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.products.data.datasources.remote.models.ProductoResponse
import com.pocketmarket.features.products.data.datasources.remote.models.TiendaDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import javax.inject.Inject

class ProductsApi @Inject constructor(
    @PocketMarketKtorClient private val client: HttpClient
) {
    suspend fun getCatalogo(search: String): List<ProductoResponse> {
        val response = client.get("productos/catalogo") {
            if (search.isNotBlank()) parameter("search", search)
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Error al cargar el catálogo.")
        }
    }

    suspend fun getTiendas(): List<TiendaDto> {
        val response = client.get("productos/tiendas")
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Error al cargar tiendas")
        }
    }
}