package com.pocketmarket.features.seller.di

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.seller.data.datasources.remote.api.SellerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SellerNetworkModule {

    @Provides
    @Singleton
    fun provideSellerApi(@PocketMarketKtorClient client: HttpClient): SellerApi {
        return SellerApi(client)
    }
}