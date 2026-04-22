package com.pocketmarket.features.cart.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.cart.domain.entities.CartItem
import com.pocketmarket.features.cart.domain.usescases.CheckoutUseCase
import com.pocketmarket.features.cart.domain.usescases.GetCartUseCase
import com.pocketmarket.features.cart.domain.usescases.RemoveFromCartUseCase
import com.pocketmarket.features.cart.domain.usescases.UpdateCartQtyUseCase
import com.pocketmarket.features.cart.presentation.screens.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val updateCartQtyUseCase: UpdateCartQtyUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _checkoutSuccess = MutableStateFlow(false)
    val checkoutSuccess: StateFlow<Boolean> = _checkoutSuccess.asStateFlow()

    init {
        cargarCarrito()
    }

    fun cargarCarrito() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val result = getCartUseCase()
            if (result.isSuccess) {
                val items = result.getOrNull() ?: emptyList()
                val total = items.sumOf { it.precio * it.cantidad }
                _uiState.value = CartUiState.Success(items, total)
            } else {
                _uiState.value = CartUiState.Error("Sesión expirada o error de red")
            }
        }
    }

    fun modificarCantidad(itemId: Int, nuevaCantidad: Int) {
        viewModelScope.launch {
            val result = updateCartQtyUseCase(itemId, nuevaCantidad)
            if (result.isSuccess) {
                cargarCarrito()
            }
        }
    }

    fun eliminarProducto(itemId: Int) {
        viewModelScope.launch {
            val result = removeFromCartUseCase(itemId)
            if (result.isSuccess) {
                cargarCarrito()
            }
        }
    }

    fun procederAlPago() {
        viewModelScope.launch {
            val result = checkoutUseCase()
            if (result.isSuccess) {
                _checkoutSuccess.value = true
                cargarCarrito()
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutSuccess.value = false
    }
}