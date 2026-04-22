package com.pocketmarket.features.admin.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.admin.domain.entities.AdminStats
import com.pocketmarket.features.admin.domain.usescases.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AdminDashboardUiState {
    object Loading : AdminDashboardUiState()
    data class Success(val stats: AdminStats) : AdminDashboardUiState()
    data class Error(val message: String) : AdminDashboardUiState()
}

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminDashboardUiState>(AdminDashboardUiState.Loading)
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init { cargarStats() }

    fun cargarStats() {
        viewModelScope.launch {
            _uiState.value = AdminDashboardUiState.Loading
            val result = getStatsUseCase()
            if (result.isSuccess) {
                _uiState.value = AdminDashboardUiState.Success(result.getOrThrow())
            } else {
                _uiState.value = AdminDashboardUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}