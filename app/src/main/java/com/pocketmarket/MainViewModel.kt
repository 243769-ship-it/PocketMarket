package com.pocketmarket

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.datastore.AuthPreferences
import com.pocketmarket.core.navigation.AdminDashboardRoute
import com.pocketmarket.core.navigation.HomeRoute
import com.pocketmarket.core.navigation.LoginRoute
import com.pocketmarket.core.navigation.SellerDashboardRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authPreferences: AuthPreferences
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination: StateFlow<Any?> = _startDestination.asStateFlow()

    init {
        determinarRutaInicial()
    }

    private fun determinarRutaInicial() {
        viewModelScope.launch {
            val token = authPreferences.accessToken.first()
            if (token.isNullOrEmpty()) {
                _startDestination.value = LoginRoute
            } else {
                val rol = getRoleFromToken(token)
                _startDestination.value = when (rol) {
                    "ADMIN" -> AdminDashboardRoute
                    "VENDEDOR" -> SellerDashboardRoute
                    else -> HomeRoute
                }
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authPreferences.clearTokens()
        }
    }

    suspend fun obtenerRutaPostLogin(): Any {
        val token = authPreferences.accessToken.first()
        val rol = getRoleFromToken(token)
        return when (rol) {
            "ADMIN" -> AdminDashboardRoute
            "VENDEDOR" -> SellerDashboardRoute
            else -> HomeRoute
        }
    }

    private fun getRoleFromToken(token: String?): String {
        if (token.isNullOrEmpty()) return "CLIENTE"
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val jsonObject = JSONObject(payload)
                jsonObject.optString("rol", "CLIENTE")
            } else "CLIENTE"
        } catch (e: Exception) { "CLIENTE" }
    }
}