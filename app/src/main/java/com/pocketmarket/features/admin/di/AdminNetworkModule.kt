package com.pocketmarket.features.admin.di

import com.pocketmarket.core.di.PocketMarketKtorClient
import com.pocketmarket.features.admin.data.datasources.remote.api.AdminApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdminNetworkModule {

    @Provides
    @Singleton
    fun provideAdminApi(@PocketMarketKtorClient client: HttpClient): AdminApi {
        return AdminApi(client)
    }
}