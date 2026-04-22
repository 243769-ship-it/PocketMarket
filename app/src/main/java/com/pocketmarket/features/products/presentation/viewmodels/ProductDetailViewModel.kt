package com.pocketmarket.features.products.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.cart.domain.usescases.AddToCartUseCase
import com.pocketmarket.features.products.domain.usescases.GetCatalogoUseCase
import com.pocketmarket.features.products.presentation.screens.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getCatalogoUseCase: GetCatalogoUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _cartState = MutableStateFlow<String?>(null)
    val cartState: StateFlow<String?> = _cartState.asStateFlow()

    init {
        val productoId = savedStateHandle.get<String>("id")?.toIntOrNull()
        if (productoId != null) {
            cargarProducto(productoId)
        } else {
            _uiState.value = DetailUiState.Error("ID inválido")
        }
    }

    private fun cargarProducto(id: Int) {
        viewModelScope.launch {
            val result = getCatalogoUseCase()
            if (result.isSuccess) {
                val lista = result.getOrNull() ?: emptyList()
                val producto = lista.find { it.id == id }
                if (producto != null) {
                    _uiState.value = DetailUiState.Success(producto)
                } else {
                    _uiState.value = DetailUiState.Error("No encontrado")
                }
            } else {
                _uiState.value = DetailUiState.Error("Error de red")
            }
        }
    }

    fun agregarAlCarrito(productoId: Int, cantidad: Int) {
        viewModelScope.launch {
            val result = addToCartUseCase(productoId, cantidad)
            if (result.isSuccess) {
                _cartState.value = "¡Agregado con éxito!"
            } else {
                _cartState.value = "Error al agregar"
            }
        }
    }

    fun clearCartMessage() { _cartState.value = null }
}