package com.pocketmarket.features.seller.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pocketmarket.core.utils.BiometricHelper
import com.pocketmarket.features.seller.presentation.viewmodels.OrdersUiState
import com.pocketmarket.features.seller.presentation.viewmodels.SellerOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerOrdersScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToMyProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SellerOrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val biometricRequest by viewModel.biometricRequest.collectAsState()
    val pocketBlue = Color(0xFF3B82F6)
    val context = LocalContext.current

    LaunchedEffect(biometricRequest) {
        biometricRequest?.let { request ->
            val titulo = if (request.second == "COMPLETADO") "Aprobar Venta" else "Rechazar Venta"
            val subtitulo = if (request.second == "COMPLETADO") "Confirma tu identidad para procesar el pedido" else "Confirma tu identidad para cancelar el pedido"

            BiometricHelper.authenticate(
                context = context,
                title = titulo,
                subtitle = subtitulo,
                onSuccess = { viewModel.procesarAccionBiometrica() },
                onError = {
                    viewModel.cancelarAutenticacion()
                    Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FB),
        topBar = { CenterAlignedTopAppBar(title = { Text("MIS PEDIDOS", fontWeight = FontWeight.Black, fontSize = 16.sp) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("Inventario") }, selected = false, onClick = onNavigateToMyProducts)
                NavigationBarItem(icon = { Icon(Icons.Default.Email, null) }, label = { Text("Pedidos") }, selected = true, onClick = onNavigateToOrders)
                NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, label = { Text("Añadir") }, selected = false, onClick = onNavigateToAddProduct)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is OrdersUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = pocketBlue)
                is OrdersUiState.Error -> Button(onClick = { viewModel.cargarPedidos() }, Modifier.align(Alignment.Center), colors = ButtonDefaults.buttonColors(containerColor = pocketBlue)) { Text("Reintentar") }
                is OrdersUiState.Success -> {
                    if (state.orders.isEmpty()) Text("Aún no tienes pedidos de clientes", Modifier.align(Alignment.Center), color = Color.Gray)
                    else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.orders) { order ->
                                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                                    Column(Modifier.padding(16.dp)) {
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Pedido #${order.id}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                            val (colorEstado, txtEstado) = when(order.estado) {
                                                "COMPLETADO" -> Color(0xFF10B981) to "ENTREGADO"
                                                "CANCELADO" -> Color.Red to "CANCELADO"
                                                else -> Color(0xFFF59E0B) to "PENDIENTE"
                                            }
                                            Surface(color = colorEstado.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                                Text(txtEstado, color = colorEstado, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                            }
                                        }
                                        Text(order.clienteEmail, color = Color.Gray, fontSize = 13.sp)
                                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color.LightGray)
                                        order.items.forEach { item ->
                                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                                AsyncImage(model = item.fotoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(45.dp).clip(RoundedCornerShape(10.dp)))
                                                Spacer(Modifier.width(12.dp))
                                                Text("${item.cantidad}x ${item.nombreProducto}", modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                            }
                                        }
                                        Spacer(Modifier.height(12.dp))
                                        Text("Total: $${order.total} MXN", fontWeight = FontWeight.Black, fontSize = 18.sp, color = pocketBlue)
                                        if (order.estado == "PENDIENTE") {
                                            Spacer(Modifier.height(16.dp))
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                                OutlinedButton(
                                                    onClick = { viewModel.solicitarAutenticacion(order.id, "CANCELADO") },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                                                ) { Text("Rechazar", color = Color.Red) }
                                                Button(
                                                    onClick = { viewModel.solicitarAutenticacion(order.id, "COMPLETADO") },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(12.dp)
                                                ) { Text("Confirmar", color = Color.White) }
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
}