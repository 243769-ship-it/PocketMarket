package com.pocketmarket.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.domain.VibratorService
import com.pocketmarket.core.hardware.data.VibrationService
import com.pocketmarket.features.seller.domain.entities.SellerProduct
import com.pocketmarket.features.seller.domain.usescases.BorrarProductoUseCase
import com.pocketmarket.features.seller.domain.usescases.EditarProductoUseCase
import com.pocketmarket.features.seller.domain.usescases.GetMisProductosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyProductsUiState {
    object Loading : MyProductsUiState()
    data class Success(val products: List<SellerProduct>) : MyProductsUiState()
    data class Error(val message: String) : MyProductsUiState()
}

@HiltViewModel
class SellerMyProductsViewModel @Inject constructor(
    private val getMisProductosUseCase: GetMisProductosUseCase,
    private val borrarProductoUseCase: BorrarProductoUseCase,
    private val editarProductoUseCase: EditarProductoUseCase,
    private val vibrationService: VibrationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyProductsUiState>(MyProductsUiState.Loading)
    val uiState: StateFlow<MyProductsUiState> = _uiState.asStateFlow()

    private val _biometricDeleteRequest = MutableStateFlow<Int?>(null)
    val biometricDeleteRequest: StateFlow<Int?> = _biometricDeleteRequest.asStateFlow()

    init { cargarMisProductos() }

    fun cargarMisProductos() {
        viewModelScope.launch {
            _uiState.value = MyProductsUiState.Loading
            getMisProductosUseCase().onSuccess { productos ->
                _uiState.value = MyProductsUiState.Success(productos)
            }.onFailure { error ->
                _uiState.value = MyProductsUiState.Error(error.message ?: "Error al cargar inventario")
            }
        }
    }

    fun solicitarBorrado(id: Int) {
        _biometricDeleteRequest.value = id
    }

    fun confirmarBorradoBiometrico() {
        val id = _biometricDeleteRequest.value ?: return
        _biometricDeleteRequest.value = null

        viewModelScope.launch {
            borrarProductoUseCase(id).onSuccess {
                vibrationService.vibrarSuave()
                cargarMisProductos()
            }
        }
    }

    fun cancelarBorrado() {
        _biometricDeleteRequest.value = null
    }

    fun editarProducto(id: Int, nombre: String, precio: Double, stock: Int, imagenBytes: ByteArray?) {
        viewModelScope.launch {
            editarProductoUseCase(id, nombre, "", precio, stock, imagenBytes).onSuccess {
                vibrationService.vibrarExito()
                cargarMisProductos()
            }
        }
    }
}