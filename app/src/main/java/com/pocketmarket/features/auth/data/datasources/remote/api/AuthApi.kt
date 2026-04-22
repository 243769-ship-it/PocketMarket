package com.pocketmarket.features.auth.data.datasources.remote.api

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.auth.data.datasources.remote.models.LoginRequest
import com.pocketmarket.features.auth.data.datasources.remote.models.LoginResponse
import com.pocketmarket.features.auth.data.datasources.remote.models.RegisterRequest
import com.pocketmarket.features.auth.data.datasources.remote.models.RegisterResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class AuthApi @Inject constructor(
    @PocketMarketKtorClient private val client: HttpClient
) {
    suspend fun login(request: LoginRequest): LoginResponse {
        val response = client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Credenciales incorrectas")
        }
    }

    suspend fun registrarCliente(request: RegisterRequest): RegisterResponse {
        val response = client.post("auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Error al registrar cliente")
        }
    }

    suspend fun registrarVendedor(
        email: String,
        pass: String,
        marca: String,
        rfc: String,
        clabe: String,
        tel: String,
        dir: String,
        ineF: ByteArray,
        ineT: ByteArray,
        comp: ByteArray
    ): RegisterResponse {
        val response = client.submitFormWithBinaryData(
            url = "auth/register",
            formData = formData {
                append("email", email)
                append("password", pass)
                append("rol", "VENDEDOR")
                append("nombreMarca", marca)
                append("rfc", rfc)
                append("clabeInterbancaria", clabe)
                append("telefono", tel)
                append("direccion", dir)
                append("ineFrente", ineF, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"frente.jpg\"")
                })
                append("ineTrasera", ineT, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"trasera.jpg\"")
                })
                append("comprobanteDom", comp, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"comprobante.jpg\"")
                })
            }
        )
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw Exception("Error al registrar vendedor")
        }
    }
}