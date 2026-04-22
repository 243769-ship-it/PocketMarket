package com.pocketmarket.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.features.auth.domain.usecases.LoginUseCase
import com.pocketmarket.features.auth.domain.usecases.RegisterClienteUseCase
import com.pocketmarket.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerClienteUseCase: RegisterClienteUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = loginUseCase(email, password)
            result.fold(
                onSuccess = { usuario ->
                    _authState.value = AuthUiState.Success(usuario)
                },
                onFailure = { error ->
                    _authState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun register(email: String, password: String, rol: String, nombreMarca: String = "") {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            if (rol == "CLIENTE") {
                val result = registerClienteUseCase(email, password)
                result.fold(
                    onSuccess = { usuario ->
                        _authState.value = AuthUiState.Success(usuario)
                    },
                    onFailure = { error ->
                        _authState.value = AuthUiState.Error(error.message ?: "Error desconocido")
                    }
                )
            }
        }
    }

    fun resetState() {
        _authState.value = AuthUiState.Idle
    }
}