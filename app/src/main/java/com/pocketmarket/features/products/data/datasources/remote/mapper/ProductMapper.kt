package com.pocketmarket.features.products.data.datasources.remote.mapper

import com.pocketmarket.features.products.data.datasources.remote.models.ProductoResponse
import com.pocketmarket.features.products.data.datasources.remote.models.TiendaDto
import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.entities.Tienda

fun ProductoResponse.toDomain(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        stock = this.stock,
        fotoUrl = this.fotoUrl,
        nombreMarca = this.vendedor.perfilVendedor?.nombreMarca ?: "Vendedor Externo"
    )
}

fun TiendaDto.toDomain(): Tienda {
    return Tienda(
        nombreMarca = this.nombreMarca,
        usuarioId = this.usuarioId
    )
}