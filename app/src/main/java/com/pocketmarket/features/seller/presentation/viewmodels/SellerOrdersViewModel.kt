package com.pocketmarket.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.domain.VibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.features.seller.domain.entities.SellerOrder
import com.pocketmarket.features.seller.domain.usescases.ActualizarEstadoPedidoUseCase
import com.pocketmarket.features.seller.domain.usescases.GetPedidosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrdersUiState {
    object Loading : OrdersUiState()
    data class Success(val orders: List<SellerOrder>) : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
}

@HiltViewModel
class SellerOrdersViewModel @Inject constructor(
    private val getPedidosUseCase: GetPedidosUseCase,
    private val actualizarEstadoPedidoUseCase: ActualizarEstadoPedidoUseCase,
    private val vibrationService: VibrationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private val _biometricRequest = MutableStateFlow<Pair<Int, String>?>(null)
    val biometricRequest: StateFlow<Pair<Int, String>?> = _biometricRequest.asStateFlow()

    init { cargarPedidos() }

    fun cargarPedidos() {
        viewModelScope.launch {
            _uiState.value = OrdersUiState.Loading
            val result = getPedidosUseCase()
            if (result.isSuccess) {
                val pedidos = result.getOrNull() ?: emptyList()
                _uiState.value = OrdersUiState.Success(pedidos)
            } else {
                val error = result.exceptionOrNull()
                _uiState.value = OrdersUiState.Error(error?.message ?: "Error al cargar pedidos")
            }
        }
    }

    fun solicitarAutenticacion(pedidoId: Int, nuevoEstado: String) {
        _biometricRequest.value = Pair(pedidoId, nuevoEstado)
    }

    fun procesarAccionBiometrica() {
        val request = _biometricRequest.value ?: return
        _biometricRequest.value = null

        viewModelScope.launch {
            val result = actualizarEstadoPedidoUseCase(request.first, request.second)
            if (result.isSuccess) {
                vibrationService.vibrarExito()
                cargarPedidos()
            }
        }
    }

    fun cancelarAutenticacion() {
        _biometricRequest.value = null
    }
}