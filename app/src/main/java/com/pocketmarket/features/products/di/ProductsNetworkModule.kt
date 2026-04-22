package com.pocketmarket.features.products.di

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.products.data.datasources.remote.api.ProductsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsNetworkModule {

    @Provides
    @Singleton
    fun provideProductsApi(@PocketMarketKtorClient client: HttpClient): ProductsApi {
        return ProductsApi(client)
    }
}