package com.pocketmarket.features.auth.presentation.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketmarket.core.hardware.data.AndroidCameraService
import com.pocketmarket.core.hardware.domain.CameraService
import com.pocketmarket.features.auth.domain.usecases.RegisterSellerUseCase
import com.pocketmarket.features.auth.presentation.screens.RegisterSellerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterSellerViewModel @Inject constructor(
    private val registerSellerUseCase: RegisterSellerUseCase,
    private val cameraService: CameraService
) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterSellerUiState>(RegisterSellerUiState.Idle)
    val uiState: StateFlow<RegisterSellerUiState> = _uiState.asStateFlow()

    fun registerSeller(
        email: String, pass: String, marca: String, rfc: String,
        clabe: String, tel: String, dir: String,
        ineFBitmap: Bitmap, ineTBitmap: Bitmap, compBitmap: Bitmap
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterSellerUiState.Loading

            val ineF = cameraService.compressBitmap(ineFBitmap)
            val ineT = cameraService.compressBitmap(ineTBitmap)
            val comp = cameraService.compressBitmap(compBitmap)

            val result = registerSellerUseCase(email, pass, marca, rfc, clabe, tel, dir, ineF, ineT, comp)

            result.fold(
                onSuccess = {
                    _uiState.value = RegisterSellerUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = RegisterSellerUiState.Error(error.message ?: "Error al registrar cuenta")
                }
            )
        }
    }
}