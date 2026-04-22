package com.pocketmarket.features.products.presentation.screens

import com.pocketmarket.features.products.domain.entities.Producto

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val producto: Producto) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}