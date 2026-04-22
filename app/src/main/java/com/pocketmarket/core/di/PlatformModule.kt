package com.pocketmarket.core.di

import android.content.Context
import com.pocketmarket.core.hardware.data.AndroidBiometricService
import com.pocketmarket.core.hardware.data.AndroidCameraService
import com.pocketmarket.core.hardware.data.AndroidFlashlightService
import com.pocketmarket.core.hardware.data.AndroidVibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.core.hardware.domain.BiometricService
import com.pocketmarket.core.hardware.domain.CameraService
import com.pocketmarket.core.hardware.domain.FlashlightService
import com.pocketmarket.core.hardware.domain.VibratorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlatformModule {

    @Provides
    @Singleton
    fun provideVibratorService(
        @ApplicationContext context: Context
    ): VibratorService = AndroidVibratorService(context)

    @Provides
    @Singleton
    fun provideBiometricService(
        @ApplicationContext context: Context
    ): BiometricService = AndroidBiometricService(context)

    @Provides
    @Singleton
    fun provideFlashlightService(
        @ApplicationContext context: Context
    ): FlashlightService = AndroidFlashlightService(context)

    @Provides
    @Singleton
    fun provideCameraService(
        @ApplicationContext context: Context
    ): CameraService = AndroidCameraService(context)

    @Provides
    @Singleton
    fun provideVibrationHelper(
        @ApplicationContext context: Context
    ): VibrationService = VibrationService(context)
}