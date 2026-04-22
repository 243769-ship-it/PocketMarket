package com.pocketmarket.features.products.domain.entities

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val fotoUrl: String,
    val nombreMarca: String
)