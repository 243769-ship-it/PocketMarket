package com.pocketmarket.features.seller.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pocketmarket.features.seller.presentation.viewmodels.AddProductUiState
import com.pocketmarket.features.seller.presentation.viewmodels.AddProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToMyProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onLogout: () -> Unit,
    viewModel: AddProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val pocketBlue = Color(0xFF3B82F6)
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    LaunchedEffect(uiState) {
        if (uiState is AddProductUiState.Success) {
            Toast.makeText(context, "¡Producto publicado con éxito!", Toast.LENGTH_SHORT).show()
            nombre = ""; descripcion = ""; precio = ""; stock = ""; imageUri = null
            viewModel.resetState()
            onNavigateToMyProducts()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FB),
        topBar = { CenterAlignedTopAppBar(title = { Text("AÑADIR PRODUCTO", fontWeight = FontWeight.Black, fontSize = 16.sp) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("Inventario") }, selected = false, onClick = onNavigateToMyProducts)
                NavigationBarItem(icon = { Icon(Icons.Default.Email, null) }, label = { Text("Pedidos") }, selected = false, onClick = onNavigateToOrders)
                NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, label = { Text("Añadir") }, selected = true, onClick = onNavigateToAddProduct)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).clickable { launcher.launch("image/*") }, contentAlignment = Alignment.Center) {
                if (imageUri != null) AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                else Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(48.dp), tint = pocketBlue)
                    Text("Toca para subir foto", color = Color.Gray, fontSize = 12.sp)
                }
            }
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del producto") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio (MXN)") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(12.dp))
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val bytes = imageUri?.let { context.contentResolver.openInputStream(it)?.readBytes() }
                    if (nombre.isNotBlank() && precio.isNotBlank() && stock.isNotBlank() && bytes != null) viewModel.agregarProducto(nombre, descripcion, precio.toDoubleOrNull() ?: 0.0, stock.toIntOrNull() ?: 0, bytes)
                    else Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = pocketBlue),
                enabled = uiState !is AddProductUiState.Loading
            ) {
                if (uiState is AddProductUiState.Loading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                else Text("PUBLICAR AHORA", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}