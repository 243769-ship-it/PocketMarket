package com.pocketmarket.features.cart.di

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.cart.data.datasources.remote.api.CartApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartNetworkModule {

    @Provides
    @Singleton
    fun provideCartApi(@PocketMarketKtorClient client: HttpClient): CartApi {
        return CartApi(client)
    }
}