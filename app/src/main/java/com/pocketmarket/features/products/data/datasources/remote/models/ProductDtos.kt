package com.pocketmarket.features.products.data.datasources.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductoResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val fotoUrl: String,
    val vendedor: VendedorInfo
)

@Serializable
data class VendedorInfo(
    val perfilVendedor: MarcaInfo?
)

@Serializable
data class MarcaInfo(
    val nombreMarca: String
)

@Serializable
data class TiendaDto(
    val nombreMarca: String,
    val usuarioId: Int
)