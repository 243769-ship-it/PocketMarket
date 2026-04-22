package com.pocketmarket.core.hardware.domain

import androidx.fragment.app.FragmentActivity

interface BiometricService {
    fun isBiometricAvailable(): Boolean
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}