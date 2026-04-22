package com.pocketmarket.features.cart.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pocketmarket.features.cart.presentation.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToCheckout: (Double) -> Unit,
    onLogout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val checkoutSuccess by viewModel.checkoutSuccess.collectAsState()
    val context = LocalContext.current
    val pocketBlue = Color(0xFF3B82F6)

    LaunchedEffect(checkoutSuccess) {
        if (checkoutSuccess) {
            Toast.makeText(context, "¡Compra confirmada!", Toast.LENGTH_SHORT).show()
            viewModel.resetCheckoutState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MI CARRITO", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp, fontSize = 16.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = pocketBlue,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column {
                if (uiState is CartUiState.Success && (uiState as CartUiState.Success).items.isNotEmpty()) {
                    val total = (uiState as CartUiState.Success).total
                    Surface(shadowElevation = 12.dp, color = Color.White) {
                        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Total a pagar", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("$${total} MXN", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFF1F2937))
                            }
                            Button(
                                onClick = { onNavigateToCheckout(total) },
                                colors = ButtonDefaults.buttonColors(containerColor = pocketBlue),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(50.dp).padding(horizontal = 8.dp)
                            ) {
                                Text("PAGAR AHORA", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToHome)
                    NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, label = { Text("Tiendas") }, selected = false, onClick = {})
                    NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, null) }, label = { Text("Carrito") }, selected = true, onClick = {})
                    NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
                }
            }
        },
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is CartUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = pocketBlue)
                is CartUiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                        Button(onClick = { viewModel.cargarCarrito() }, colors = ButtonDefaults.buttonColors(containerColor = pocketBlue)) {
                            Text("Reintentar")
                        }
                    }
                }
                is CartUiState.Success -> {
                    if (state.items.isEmpty()) {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                            Spacer(Modifier.height(16.dp))
                            Text("Tu carrito está vacío", color = Color.Gray, fontWeight = FontWeight.Medium)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.items) { item ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = item.fotoUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
                                        )
                                        Spacer(Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1F2937), maxLines = 1)
                                            Text("$${item.precio} MXN", color = pocketBlue, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                                Surface(shape = CircleShape, color = Color(0xFFF3F4F7), modifier = Modifier.size(32.dp).clickable { viewModel.modificarCantidad(item.id, item.cantidad - 1) }) {
                                                    Box(contentAlignment = Alignment.Center) { Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black) }
                                                }
                                                Text("${item.cantidad}", modifier = Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.Black)
                                                Surface(shape = CircleShape, color = Color(0xFFF3F4F7), modifier = Modifier.size(32.dp).clickable { viewModel.modificarCantidad(item.id, item.cantidad + 1) }) {
                                                    Box(contentAlignment = Alignment.Center) { Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black) }
                                                }
                                            }
                                        }
                                        IconButton(onClick = { viewModel.eliminarProducto(item.id) }) {
                                            Icon(Icons.Default.Delete, null, tint = Color(0xFFFF5252))
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