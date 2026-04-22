package com.pocketmarket.features.products.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pocketmarket.features.products.presentation.viewmodels.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(onBack: () -> Unit, viewModel: ProductDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val cartState by viewModel.cartState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(cartState) {
        cartState?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearCartMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DETALLE DEL PRODUCTO", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is DetailUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color(0xFF3B82F6))
                is DetailUiState.Success -> {
                    val p = state.producto
                    var qty by remember { mutableStateOf(1) }
                    Column(Modifier.fillMaxSize().padding(24.dp)) {
                        AsyncImage(
                            model = p.fotoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(320.dp).clip(RoundedCornerShape(24.dp))
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(p.nombreMarca.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                        Text(p.nombre, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        Text("$${p.precio} MXN", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                        Spacer(Modifier.height(16.dp))
                        Text(p.descripcion, color = Color.DarkGray, lineHeight = 22.sp)
                        Spacer(Modifier.weight(1f))
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Row(
                                modifier = Modifier.background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)).padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { if (qty > 1) qty-- }) { Text("-", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                                Text("$qty", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                                IconButton(onClick = { if (qty < p.stock) qty++ }) { Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                            }
                            Spacer(Modifier.width(16.dp))
                            Button(
                                onClick = { viewModel.agregarAlCarrito(p.id, qty) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) { Text("AÑADIR AL CARRITO", fontWeight = FontWeight.Bold, color = Color.White) }
                        }
                    }
                }
                is DetailUiState.Error -> Text(state.message, Modifier.align(Alignment.Center), color = Color.Red)
            }
        }
    }
}