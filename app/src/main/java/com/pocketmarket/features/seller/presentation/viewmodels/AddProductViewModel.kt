package com.pocketmarket.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.domain.VibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.features.seller.domain.usescases.CrearProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddProductUiState {
    object Idle : AddProductUiState()
    object Loading : AddProductUiState()
    object Success : AddProductUiState()
    data class Error(val message: String) : AddProductUiState()
}

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val crearProductoUseCase: CrearProductoUseCase,
    private val vibrationService: VibrationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddProductUiState>(AddProductUiState.Idle)
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun agregarProducto(nombre: String, desc: String, precio: Double, stock: Int, imagenBytes: ByteArray) {
        viewModelScope.launch {
            _uiState.value = AddProductUiState.Loading
            crearProductoUseCase(nombre, desc, precio, stock, imagenBytes).onSuccess {
                vibrationService.vibrarExito()
                _uiState.value = AddProductUiState.Success
            }.onFailure { error ->
                vibrationService.vibrarError()
                _uiState.value = AddProductUiState.Error(error.message ?: "Error al publicar")
            }
        }
    }

    fun resetState() { _uiState.value = AddProductUiState.Idle }
}