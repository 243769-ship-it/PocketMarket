package com.pocketmarket.features.auth.di

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.auth.data.datasources.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApi(@PocketMarketKtorClient client: HttpClient): AuthApi {
        return AuthApi(client)
    }
}