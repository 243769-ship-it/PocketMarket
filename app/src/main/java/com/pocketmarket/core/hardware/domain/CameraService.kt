package com.pocketmarket.core.hardware.domain

import android.graphics.Bitmap

interface CameraService {
    fun hasCameraPermission(): Boolean
    fun compressBitmap(bitmap: Bitmap, quality: Int = 90): ByteArray
}