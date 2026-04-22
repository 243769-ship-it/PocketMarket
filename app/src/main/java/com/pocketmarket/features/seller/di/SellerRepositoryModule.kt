package com.pocketmarket.features.seller.di

import com.pocketmarket.features.seller.data.repositories.SellerRepositoryImpl
import com.pocketmarket.features.seller.domain.repositories.SellerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SellerRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSellerRepository(
        sellerRepositoryImpl: SellerRepositoryImpl
    ): SellerRepository
}