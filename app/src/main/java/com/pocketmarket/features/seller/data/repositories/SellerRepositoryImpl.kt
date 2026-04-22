package com.pocketmarket.features.seller.data.repositories

import com.pocketmarket.features.seller.data.datasources.remote.api.SellerApi
import com.pocketmarket.features.seller.data.datasources.remote.mapper.toDomain
import com.pocketmarket.features.seller.domain.entities.SellerDashboardStats
import com.pocketmarket.features.seller.domain.entities.SellerOrder
import com.pocketmarket.features.seller.domain.entities.SellerProduct
import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
    private val api: SellerApi
) : SellerRepository {

    override suspend fun getDashboardStats(): SellerDashboardStats {
        val response = api.getDashboardStats()
        return response.toDomain()
    }

    override suspend fun getMisProductos(): List<SellerProduct> {
        val response = api.getMisProductos()
        return response.map { it.toDomain() }
    }

    override suspend fun crearProducto(nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray) {
        api.crearProducto(nombre, desc, precio, stock, imagenBytes)
    }

    override suspend fun getPedidos(): List<SellerOrder> {
        val response = api.getPedidos()
        return response.map { it.toDomain() }
    }

    override suspend fun actualizarEstadoPedido(pedidoId: Int, estado: String) {
        api.actualizarEstadoPedido(pedidoId, estado)
    }

    override suspend fun borrarProducto(id: Int) {
        api.borrarProducto(id)
    }

    override suspend fun editarProducto(id: Int, nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray?) {
        api.editarProducto(id, nombre, desc, precio, stock, imagenBytes)
    }
}