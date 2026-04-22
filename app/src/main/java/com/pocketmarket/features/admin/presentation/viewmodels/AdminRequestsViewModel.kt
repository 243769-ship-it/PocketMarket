package com.pocketmarket.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.domain.VibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.domain.usescases.AprobarVendedorUseCase
import com.pocketmarket.features.admin.domain.usescases.GetVendedoresPendientesUseCase
import com.pocketmarket.features.admin.domain.usescases.RechazarVendedorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminRequestsUiState {
    object Loading : AdminRequestsUiState()
    data class Success(val requests: List<AdminSeller>) : AdminRequestsUiState()
    data class Error(val message: String) : AdminRequestsUiState()
}

@HiltViewModel
class AdminRequestsViewModel @Inject constructor(
    private val getVendedoresPendientesUseCase: GetVendedoresPendientesUseCase,
    private val aprobarVendedorUseCase: AprobarVendedorUseCase,
    private val rechazarVendedorUseCase: RechazarVendedorUseCase,
    private val vibrationService: VibrationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminRequestsUiState>(AdminRequestsUiState.Loading)
    val uiState: StateFlow<AdminRequestsUiState> = _uiState.asStateFlow()

    private val _biometricRequest = MutableStateFlow<Pair<Int, String?>?>(null)
    val biometricRequest: StateFlow<Pair<Int, String?>?> = _biometricRequest.asStateFlow()

    init { cargarSolicitudes() }

    fun cargarSolicitudes() {
        viewModelScope.launch {
            _uiState.value = AdminRequestsUiState.Loading
            val result = getVendedoresPendientesUseCase()
            if (result.isSuccess) {
                _uiState.value = AdminRequestsUiState.Success(result.getOrThrow())
            } else {
                _uiState.value = AdminRequestsUiState.Error(result.exceptionOrNull()?.message ?: "Error")
            }
        }
    }

    fun solicitarAutenticacion(usuarioId: Int, mensajeRechazo: String? = null) {
        _biometricRequest.value = Pair(usuarioId, mensajeRechazo)
    }

    fun procesarAccionBiometrica() {
        val request = _biometricRequest.value ?: return
        _biometricRequest.value = null
        val (id, mensaje) = request

        viewModelScope.launch {
            val result = if (mensaje == null) aprobarVendedorUseCase(id) else rechazarVendedorUseCase(id, mensaje)
            if (result.isSuccess) {
                vibrationService.vibrarExito()
                cargarSolicitudes()
            } else {
                vibrationService.vibrarError()
            }
        }
    }

    fun cancelarAutenticacion() { _biometricRequest.value = null }
}