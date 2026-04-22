package com.pocketmarket.features.admin.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketmarket.core.utils.BiometricHelper
import com.pocketmarket.features.admin.presentation.viewmodels.AdminSellersUiState
import com.pocketmarket.features.admin.presentation.viewmodels.AdminSellersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSellersScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToSellers: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminSellersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val biometricRequest by viewModel.biometricRequest.collectAsState()
    val context = LocalContext.current
    val primaryColor = Color(0xFF1E3A8A)

    LaunchedEffect(biometricRequest) {
        biometricRequest?.let {
            BiometricHelper.authenticate(
                context = context,
                title = "Seguridad de Administrador",
                subtitle = "Confirma tu identidad para cambiar el estado del vendedor",
                onSuccess = { viewModel.procesarAccionBiometrica() },
                onError = { viewModel.cancelarAutenticacion() }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FB),
        topBar = { CenterAlignedTopAppBar(title = { Text("GESTIÓN DE VENDEDORES", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, letterSpacing = 1.sp) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Solicitudes") }, selected = false, onClick = onNavigateToRequests)
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Vendedores") }, selected = true, onClick = onNavigateToSellers)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is AdminSellersUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = primaryColor)
                is AdminSellersUiState.Error -> Button(onClick = { viewModel.cargarVendedores() }, Modifier.align(Alignment.Center)) { Text("Reintentar") }
                is AdminSellersUiState.Success -> {
                    if (state.sellers.isEmpty()) Text("No hay vendedores", Modifier.align(Alignment.Center), color = Color.Gray)
                    else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.sellers) { seller ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(20.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Box(Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFF3F4F7)), contentAlignment = Alignment.Center) {
                                            Text(seller.nombreMarca.take(1).uppercase(), fontWeight = FontWeight.Black, fontSize = 20.sp, color = primaryColor)
                                        }
                                        Spacer(Modifier.width(16.dp))
                                        Column(Modifier.weight(1f)) {
                                            Text(seller.nombreMarca, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                            Text(seller.email, color = Color.Gray, fontSize = 12.sp)
                                            Spacer(Modifier.height(4.dp))
                                            Surface(
                                                color = if (seller.estado == "APROBADO") Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(
                                                    text = if (seller.estado == "APROBADO") "ACTIVO" else "BLOQUEADO",
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (seller.estado == "APROBADO") Color(0xFF065F46) else Color(0xFF991B1B)
                                                )
                                            }
                                        }
                                        IconButton(
                                            onClick = { viewModel.solicitarAutenticacion(seller.usuarioId, seller.estado == "APROBADO") },
                                            colors = IconButtonDefaults.iconButtonColors(containerColor = if (seller.estado == "APROBADO") Color(0xFFFEE2E2) else Color(0xFFD1FAE5))
                                        ) {
                                            Icon(
                                                imageVector = if (seller.estado == "APROBADO") Icons.Default.Lock else Icons.Default.Check,
                                                contentDescription = null,
                                                tint = if (seller.estado == "APROBADO") Color.Red else Color(0xFF059669)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}