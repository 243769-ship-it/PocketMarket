package com.pocketmarket.features.seller.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.seller.domain.entities.SellerDashboardStats
import com.pocketmarket.features.seller.domain.usescases.GetDashboardStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val stats: SellerDashboardStats) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

@HiltViewModel
class SellerDashboardViewModel @Inject constructor(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        cargarStats()
    }

    fun cargarStats() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val result = getDashboardStatsUseCase()
            if (result.isSuccess) {
                val stats = result.getOrNull()
                if (stats != null) {
                    _uiState.value = DashboardUiState.Success(stats)
                } else {
                    _uiState.value = DashboardUiState.Error("Error al cargar stats")
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.value = DashboardUiState.Error(error?.message ?: "Error desconocido")
            }
        }
    }
}