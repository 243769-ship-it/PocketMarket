package com.pocketmarket.features.auth.presentation.screens

import com.pocketmarket.features.auth.domain.entities.Usuario

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val usuario: Usuario) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

sealed class RegisterSellerUiState {
    object Idle : RegisterSellerUiState()
    object Loading : RegisterSellerUiState()
    object Success : RegisterSellerUiState()
    data class Error(val message: String) : RegisterSellerUiState()
}