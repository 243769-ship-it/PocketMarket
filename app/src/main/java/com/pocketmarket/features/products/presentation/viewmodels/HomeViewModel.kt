package com.pocketmarket.features.products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.products.domain.entities.Producto
import com.pocketmarket.features.products.domain.usescases.GetCatalogoUseCase
import com.pocketmarket.features.products.domain.usescases.GetFavoriteIdsUseCase
import com.pocketmarket.features.products.domain.usescases.GetTiendasUseCase
import com.pocketmarket.features.products.domain.usescases.ToggleFavoriteUseCase
import com.pocketmarket.features.products.presentation.screens.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCatalogoUseCase: GetCatalogoUseCase,
    private val getTiendasUseCase: GetTiendasUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favoriteIds: StateFlow<Set<Int>> = getFavoriteIdsUseCase()
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var searchJob: Job? = null

    init {
        cargarDatos()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            cargarDatos()
        }
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val productosResult = getCatalogoUseCase(_searchQuery.value)
            val tiendasResult = getTiendasUseCase()

            if (productosResult.isSuccess && tiendasResult.isSuccess) {
                _uiState.value = HomeUiState.Success(
                    productosResult.getOrNull() ?: emptyList(),
                    tiendasResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = HomeUiState.Error("Error al cargar los datos")
            }
        }
    }

    fun toggleFavorite(producto: Producto, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(producto, isFavorite)
        }
    }
}