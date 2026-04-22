package com.pocketmarket.features.seller.domain.repositories

import com.pocketmarket.features.seller.domain.entities.SellerDashboardStats
import com.pocketmarket.features.seller.domain.entities.SellerOrder
import com.pocketmarket.features.seller.domain.entities.SellerProduct

interface SellerRepository {
    suspend fun getDashboardStats(): SellerDashboardStats
    suspend fun getMisProductos(): List<SellerProduct>
    suspend fun crearProducto(nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray)
    suspend fun getPedidos(): List<SellerOrder>
    suspend fun actualizarEstadoPedido(pedidoId: Int, estado: String)
    suspend fun borrarProducto(id: Int)
    suspend fun editarProducto(id: Int, nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray?)
}