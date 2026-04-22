package com.pocketmarket.features.auth.data.repositories

import com.pocketmarket.core.datastore.AuthPreferences
import com.pocketmarket.features.auth.data.datasources.remote.api.AuthApi
import com.pocketmarket.features.auth.data.datasources.remote.mapper.toDomain
import com.pocketmarket.features.auth.data.datasources.remote.models.LoginRequest
import com.pocketmarket.features.auth.data.datasources.remote.models.RegisterRequest
import com.pocketmarket.features.auth.domain.entities.Usuario
import com.pocketmarket.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): Usuario {
        val response = api.login(LoginRequest(email, password))
        authPreferences.saveTokens(response.accessToken, response.refreshToken)
        return response.usuario.toDomain()
    }

    override suspend fun registrarCliente(email: String, password: String): Usuario {
        val response = api.registrarCliente(RegisterRequest(email, password, "CLIENTE"))
        return response.usuario.toDomain()
    }

    override suspend fun registrarVendedor(
        email: String, pass: String, marca: String, rfc: String,
        clabe: String, tel: String, dir: String,
        ineF: ByteArray, ineT: ByteArray, comp: ByteArray
    ): Usuario {
        val response = api.registrarVendedor(email, pass, marca, rfc, clabe, tel, dir, ineF, ineT, comp)
        return response.usuario.toDomain()
    }

    override suspend fun logout() {
        authPreferences.clearTokens()
    }
}