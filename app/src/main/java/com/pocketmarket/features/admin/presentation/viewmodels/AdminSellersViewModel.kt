package com.pocketmarket.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.domain.VibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.domain.usescases.AprobarVendedorUseCase
import com.pocketmarket.features.admin.domain.usescases.GetVendedoresActivosUseCase
import com.pocketmarket.features.admin.domain.usescases.RechazarVendedorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminSellersUiState {
    object Loading : AdminSellersUiState()
    data class Success(val sellers: List<AdminSeller>) : AdminSellersUiState()
    data class Error(val message: String) : AdminSellersUiState()
}

@HiltViewModel
class AdminSellersViewModel @Inject constructor(
    private val getVendedoresActivosUseCase: GetVendedoresActivosUseCase,
    private val aprobarVendedorUseCase: AprobarVendedorUseCase,
    private val rechazarVendedorUseCase: RechazarVendedorUseCase,
    private val vibrationService: VibrationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminSellersUiState>(AdminSellersUiState.Loading)
    val uiState: StateFlow<AdminSellersUiState> = _uiState.asStateFlow()

    private val _biometricRequest = MutableStateFlow<Pair<Int, Boolean>?>(null)
    val biometricRequest: StateFlow<Pair<Int, Boolean>?> = _biometricRequest.asStateFlow()

    init { cargarVendedores() }

    fun cargarVendedores() {
        viewModelScope.launch {
            _uiState.value = AdminSellersUiState.Loading
            val result = getVendedoresActivosUseCase()
            if (result.isSuccess) {
                _uiState.value = AdminSellersUiState.Success(result.getOrThrow())
            } else {
                _uiState.value = AdminSellersUiState.Error(result.exceptionOrNull()?.message ?: "Error")
            }
        }
    }

    fun solicitarAutenticacion(usuarioId: Int, bloquear: Boolean) {
        _biometricRequest.value = Pair(usuarioId, bloquear)
    }

    fun procesarAccionBiometrica() {
        val request = _biometricRequest.value ?: return
        _biometricRequest.value = null
        val (id, bloquear) = request

        viewModelScope.launch {
            val result = if (bloquear) rechazarVendedorUseCase(id, "Bloqueado por Admin") else aprobarVendedorUseCase(id)
            if (result.isSuccess) {
                vibrationService.vibrarExito()
                cargarVendedores()
            } else {
                vibrationService.vibrarError()
            }
        }
    }

    fun cancelarAutenticacion() { _biometricRequest.value = null }
}