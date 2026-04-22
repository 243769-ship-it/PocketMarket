package com.pocketmarket.features.seller.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketmarket.features.seller.presentation.components.SellerStatCard
import com.pocketmarket.features.seller.presentation.viewmodels.DashboardUiState
import com.pocketmarket.features.seller.presentation.viewmodels.SellerDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToMyProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SellerDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pocketBlue = Color(0xFF3B82F6)

    Scaffold(
        containerColor = Color(0xFFF8F9FB),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PANEL DE VENDEDOR", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp) },
                actions = { IconButton(onClick = { viewModel.cargarStats() }) { Icon(Icons.Default.Refresh, null) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = true, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("Inventario") }, selected = false, onClick = onNavigateToMyProducts)
                NavigationBarItem(icon = { Icon(Icons.Default.Email, null) }, label = { Text("Pedidos") }, selected = false, onClick = onNavigateToOrders)
                NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, label = { Text("Añadir") }, selected = false, onClick = onNavigateToAddProduct)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is DashboardUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = pocketBlue)
                is DashboardUiState.Error -> Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.message, color = Color.Red)
                    Button(onClick = { viewModel.cargarStats() }, colors = ButtonDefaults.buttonColors(containerColor = pocketBlue)) { Text("Reintentar") }
                }
                is DashboardUiState.Success -> Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Resumen de cuenta", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    SellerStatCard(
                        title = "Ventas Totales",
                        value = "$${state.stats.totalVentas} MXN",
                        icon = Icons.Default.Star,
                        color = Color(0xFF10B981)
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SellerStatCard(
                            title = "Pendientes",
                            value = "${state.stats.pedidosPendientes}",
                            icon = Icons.Default.List,
                            color = Color(0xFFF59E0B),
                            modifier = Modifier.weight(1f)
                        )
                        SellerStatCard(
                            title = "Productos",
                            value = "${state.stats.productosActivos}",
                            icon = Icons.Default.ShoppingCart,
                            color = pocketBlue,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}