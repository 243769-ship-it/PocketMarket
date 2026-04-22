package com.pocketmarket.features.products.presentation.screens

import com.pocketmarket.features.products.data.local.entities.FavoriteProductEntity

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val favorites: List<FavoriteProductEntity>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}