package com.pocketmarket.features.products.presentation.screens

import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.entities.Tienda

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val productos: List<Producto>, val tiendas: List<Tienda>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}