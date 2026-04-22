package com.pocketmarket.features.admin.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketmarket.features.admin.domain.entities.RevenueMonth
import com.pocketmarket.features.admin.presentation.components.AdminStatCard
import com.pocketmarket.features.admin.presentation.viewmodels.AdminDashboardUiState
import com.pocketmarket.features.admin.presentation.viewmodels.AdminDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToSellers: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val primaryNavyBlue = Color(0xFF1E3A8A)

    Scaffold(
        containerColor = Color(0xFFF3F4F7),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = true, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Solicitudes") }, selected = false, onClick = onNavigateToRequests)
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Vendedores") }, selected = false, onClick = onNavigateToSellers)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is AdminDashboardUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = primaryNavyBlue)
                is AdminDashboardUiState.Error -> Button(onClick = { viewModel.cargarStats() }, Modifier.align(Alignment.Center)) { Text("Reintentar") }
                is AdminDashboardUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Dashboard Global", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.Black)
                            IconButton(onClick = { viewModel.cargarStats() }, modifier = Modifier.background(Color.White, CircleShape)) {
                                Icon(Icons.Default.Refresh, contentDescription = "Recargar", tint = Color.Black)
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            AdminStatCard(
                                title = "Ingresos Totales",
                                value = "$${state.stats.statsGraficas.totalRevenue}",
                                subtitleText = "Estable",
                                subtitleIcon = Icons.Default.KeyboardArrowUp,
                                containerColor = primaryNavyBlue,
                                titleColor = Color(0xFF93C5FD),
                                valueColor = Color.White,
                                subtitleColor = Color(0xFF34D399),
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                title = "Usuarios Activos",
                                value = "${state.stats.statsGraficas.activeUsers}",
                                subtitleText = "${state.stats.numerosPrincipales.totalVendedores} Vendedores",
                                subtitleIcon = Icons.Default.Person,
                                containerColor = Color.White,
                                titleColor = Color.Gray,
                                valueColor = Color.Black,
                                subtitleColor = Color.Gray,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Resumen de Ingresos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                Spacer(Modifier.height(24.dp))
                                CustomBarChart(data = state.stats.statsGraficas.revenueByMonth, barColor = primaryNavyBlue)
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Productos Más Vendidos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                Spacer(Modifier.height(16.dp))

                                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Producto", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(2f))
                                    Text("Precio", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                    Text("Estado", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                                }

                                state.stats.statsGraficas.topSellingProducts.forEach { product ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(2f)) {
                                            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF3F4F7)), contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.ShoppingCart, null, tint = Color.Gray)
                                            }
                                            Spacer(Modifier.width(12.dp))
                                            Text(product.nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        }
                                        Text("$${product.precio}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = primaryNavyBlue, modifier = Modifier.weight(1f))
                                        Box(
                                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE5E7EB)).padding(vertical = 4.dp, horizontal = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Activo", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
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

@Composable
fun CustomBarChart(data: List<RevenueMonth>, barColor: Color) {
    if (data.isEmpty()) return

    val maxValue = data.maxOf { it.value }.coerceAtLeast(1.0)

    Row(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { monthData ->
            val fillHeightPercentage = (monthData.value / maxValue).toFloat()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(fillHeightPercentage)) {
                        drawRoundRect(
                            color = barColor,
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(monthData.label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}