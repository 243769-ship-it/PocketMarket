package com.pocketmarket.features.admin.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pocketmarket.core.utils.BiometricHelper
import com.pocketmarket.features.admin.domain.entities.AdminSeller
import com.pocketmarket.features.admin.presentation.viewmodels.AdminRequestsUiState
import com.pocketmarket.features.admin.presentation.viewmodels.AdminRequestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestsScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToSellers: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminRequestsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val biometricRequest by viewModel.biometricRequest.collectAsState()
    val context = LocalContext.current

    var showRejectDialog by remember { mutableStateOf<AdminSeller?>(null) }
    var rejectReason by remember { mutableStateOf("") }
    var expandedImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(biometricRequest) {
        biometricRequest?.let {
            BiometricHelper.authenticate(
                context = context,
                title = "Confirmar Acción",
                subtitle = "Autentícate para procesar esta solicitud",
                onSuccess = { viewModel.procesarAccionBiometrica() },
                onError = { viewModel.cancelarAutenticacion() }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFF3F4F7),
        topBar = { TopAppBar(title = { Text("Solicitudes Pendientes", fontWeight = FontWeight.Black) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Solicitudes") }, selected = true, onClick = onNavigateToRequests)
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Vendedores") }, selected = false, onClick = onNavigateToSellers)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is AdminRequestsUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is AdminRequestsUiState.Error -> Button(onClick = { viewModel.cargarSolicitudes() }, Modifier.align(Alignment.Center)) { Text("Reintentar") }
                is AdminRequestsUiState.Success -> {
                    if (state.requests.isEmpty()) Text("No hay solicitudes", Modifier.align(Alignment.Center), color = Color.Gray)
                    else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.requests) { request ->
                                RequestCard(
                                    seller = request,
                                    onApprove = { viewModel.solicitarAutenticacion(request.usuarioId) },
                                    onReject = { showRejectDialog = request },
                                    onImageClick = { expandedImageUrl = it }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showRejectDialog != null) {
            AlertDialog(
                onDismissRequest = { showRejectDialog = null },
                title = { Text("Rechazar Solicitud") },
                text = { OutlinedTextField(value = rejectReason, onValueChange = { rejectReason = it }, label = { Text("Motivo") }) },
                confirmButton = {
                    Button(onClick = {
                        viewModel.solicitarAutenticacion(showRejectDialog!!.usuarioId, rejectReason)
                        showRejectDialog = null; rejectReason = ""
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Rechazar") }
                },
                dismissButton = { TextButton(onClick = { showRejectDialog = null }) { Text("Cancelar") } }
            )
        }

        if (expandedImageUrl != null) {
            Dialog(onDismissRequest = { expandedImageUrl = null }) {
                AsyncImage(model = expandedImageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)))
            }
        }
    }
}

@Composable
fun RequestCard(seller: AdminSeller, onApprove: () -> Unit, onReject: () -> Unit, onImageClick: (String) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(seller.nombreMarca, fontWeight = FontWeight.Black, fontSize = 18.sp)
            Text(seller.email, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(seller.ineFrenteUrl, seller.ineTraseraUrl, seller.comprobanteUrl).forEach { url ->
                    AsyncImage(model = url, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).clickable { onImageClick(url) }, contentScale = ContentScale.Crop)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)), modifier = Modifier.weight(1f)) { Text("Rechazar", color = Color.Red) }
                Button(onClick = onApprove, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)), modifier = Modifier.weight(1f)) { Text("Aprobar") }
            }
        }
    }
}