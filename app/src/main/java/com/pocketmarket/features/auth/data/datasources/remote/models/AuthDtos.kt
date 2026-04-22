package com.pocketmarket.features.auth.data.datasources.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val rol: String,
    val nombreMarca: String? = null
)

@Serializable
data class UsuarioDto(
    val id: Int,
    val email: String,
    val rol: String
)

@Serializable
data class LoginResponse(
    val mensaje: String,
    val accessToken: String,
    val refreshToken: String,
    val usuario: UsuarioDto
)

@Serializable
data class RegisterResponse(
    val mensaje: String,
    val usuario: UsuarioDto
)