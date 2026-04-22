package com.pocketmarket.core.hardware.domain

interface FlashlightService {
    fun blinkFlash(durationMillis: Long)
    fun turnOn()
    fun turnOff()
}