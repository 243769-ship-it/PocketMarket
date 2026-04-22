package com.pocketmarket.core.di

import com.pocketmarket.core.datastore.AuthPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Serializable
data class RefreshRequest(val refreshToken: String)

@Serializable
data class RefreshResponse(val accessToken: String)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @PocketMarketKtorClient
    fun provideKtorClient(authPreferences: AuthPreferences): HttpClient {
        return HttpClient(Android) {
            install(Logging) { level = LogLevel.ALL }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Auth) {
                bearer {
                    sendWithoutRequest { request ->
                        !request.url.encodedPath.contains("auth")
                    }

                    loadTokens {
                        val token = authPreferences.accessToken.first()
                        val refresh = authPreferences.refreshToken.first()
                        if (!token.isNullOrEmpty() && !refresh.isNullOrEmpty()) {
                            BearerTokens(token, refresh)
                        } else null
                    }

                    refreshTokens {
                        val refresh = authPreferences.refreshToken.first() ?: return@refreshTokens null

                        val response = client.post("auth/refresh") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(refresh))
                        }

                        if (response.status.isSuccess()) {
                            val newTokens: RefreshResponse = response.body()
                            authPreferences.saveAccessToken(newTokens.accessToken)
                            BearerTokens(newTokens.accessToken, refresh)
                        } else {
                            authPreferences.clearTokens()
                            null
                        }
                    }
                }
            }

            defaultRequest {
                url("http://35.172.54.44:3000/api/")
            }
        }
    }

    @Provides
    @Singleton
    fun provideUnqualifiedKtorClient(
        @PocketMarketKtorClient client: HttpClient
    ): HttpClient {
        return client
    }
}