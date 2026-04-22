package com.pocketmarket.features.auth.domain.repositories

import com.pocketmarket.features.auth.domain.entities.Usuario

interface AuthRepository {
    suspend fun login(email: String, password: String): Usuario
    suspend fun registrarCliente(email: String, password: String): Usuario
    suspend fun registrarVendedor(
        email: String, pass: String, marca: String, rfc: String,
        clabe: String, tel: String, dir: String,
        ineF: ByteArray, ineT: ByteArray, comp: ByteArray
    ): Usuario
    suspend fun logout()
}