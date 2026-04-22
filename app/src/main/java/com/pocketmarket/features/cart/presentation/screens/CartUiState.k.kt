package com.pocketmarket.features.cart.presentation.screens

import com.pocketmarket.features.cart.domain.entities.CartItem

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val items: List<CartItem>, val total: Double) : CartUiState()
    data class Error(val message: String) : CartUiState()
}