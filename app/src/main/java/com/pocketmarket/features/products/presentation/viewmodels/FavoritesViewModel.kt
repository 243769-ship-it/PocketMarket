package com.pocketmarket.features.products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.products.domain.usescases.GetFavoriteProductsUseCase
import com.pocketmarket.features.products.presentation.screens.FavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            getFavoriteProductsUseCase()
                .catch { e ->
                    _uiState.value = FavoritesUiState.Error(e.message ?: "Error al cargar favoritos")
                }
                .collect { lista ->
                    _uiState.value = FavoritesUiState.Success(lista)
                }
        }
    }
}