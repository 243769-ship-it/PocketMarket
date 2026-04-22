package com.pocketmarket.features.seller.data.datasources.remote.mapper

import com.pocketmarket.features.seller.data.datasources.remote.models.SellerDashboardResponse
import com.pocketmarket.features.seller.data.datasources.remote.models.SellerOrderDto
import com.pocketmarket.features.seller.data.datasources.remote.models.SellerProductDto
import com.pocketmarket.features.seller.domain.entities.SellerDashboardStats
import com.pocketmarket.features.seller.domain.entities.SellerOrder
import com.pocketmarket.features.seller.domain.entities.SellerOrderItem
import com.pocketmarket.features.seller.domain.entities.SellerProduct

fun SellerDashboardResponse.toDomain(): SellerDashboardStats {
    return SellerDashboardStats(
        totalVentas = this.totalVentas,
        pedidosPendientes = this.pedidosPendientes,
        productosActivos = this.productosActivos
    )
}

fun SellerProductDto.toDomain(): SellerProduct {
    return SellerProduct(
        id = this.id,
        nombre = this.nombre,
        precio = this.precio,
        stock = this.stock,
        fotoUrl = this.fotoUrl
    )
}

fun SellerOrderDto.toDomain(): SellerOrder {
    return SellerOrder(
        id = this.id,
        total = this.total,
        estado = this.estado,
        clienteEmail = this.cliente.email,
        items = this.items.map {
            SellerOrderItem(
                nombreProducto = it.producto.nombre,
                fotoUrl = it.producto.fotoUrl,
                cantidad = it.cantidad,
                precio = it.precio
            )
        }
    )
}