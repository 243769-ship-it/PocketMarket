package com.pocketmarket.features.auth.data.datasources.remote.mapper

import com.pocketmarket.features.auth.data.datasources.remote.models.UsuarioDto
import com.pocketmarket.features.auth.domain.entities.Usuario

fun UsuarioDto.toDomain(): Usuario {
    return Usuario(
        id = this.id,
        email = this.email,
        rol = this.rol
    )
}