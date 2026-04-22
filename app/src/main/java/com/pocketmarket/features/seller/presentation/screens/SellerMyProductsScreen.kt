package com.pocketmarket.features.seller.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.pocketmarket.core.utils.BiometricHelper
import com.pocketmarket.features.seller.domain.entities.SellerProduct
import com.pocketmarket.features.seller.presentation.viewmodels.MyProductsUiState
import com.pocketmarket.features.seller.presentation.viewmodels.SellerMyProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerMyProductsScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToMyProducts: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SellerMyProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val biometricRequest by viewModel.biometricDeleteRequest.collectAsState()
    val context = LocalContext.current
    val pocketBlue = Color(0xFF3B82F6)
    var productToEdit by remember { mutableStateOf<SellerProduct?>(null) }

    LaunchedEffect(biometricRequest) {
        if (biometricRequest != null) {
            BiometricHelper.authenticate(
                context = context,
                title = "Eliminar Producto",
                subtitle = "Confirma tu identidad para borrar este producto",
                onSuccess = { viewModel.confirmarBorradoBiometrico() },
                onError = {
                    viewModel.cancelarBorrado()
                    Toast.makeText(context, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FB),
        topBar = { CenterAlignedTopAppBar(title = { Text("MI INVENTARIO", fontWeight = FontWeight.Black, fontSize = 16.sp) }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }, selected = false, onClick = onNavigateToDashboard)
                NavigationBarItem(icon = { Icon(Icons.Default.List, null) }, label = { Text("Inventario") }, selected = true, onClick = onNavigateToMyProducts)
                NavigationBarItem(icon = { Icon(Icons.Default.Email, null) }, label = { Text("Pedidos") }, selected = false, onClick = onNavigateToOrders)
                NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, label = { Text("Añadir") }, selected = false, onClick = onNavigateToAddProduct)
                NavigationBarItem(icon = { Icon(Icons.Default.ExitToApp, null) }, label = { Text("Salir") }, selected = false, onClick = onLogout)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is MyProductsUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = pocketBlue)
                is MyProductsUiState.Error -> Button(onClick = { viewModel.cargarMisProductos() }, Modifier.align(Alignment.Center), colors = ButtonDefaults.buttonColors(containerColor = pocketBlue)) { Text("Reintentar") }
                is MyProductsUiState.Success -> {
                    if (state.products.isEmpty()) Text("No tienes productos publicados", Modifier.align(Alignment.Center), color = Color.Gray)
                    else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(state.products) { producto ->
                                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(model = producto.fotoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)))
                                        Spacer(Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                                            Text("$${producto.precio} MXN", color = pocketBlue, fontWeight = FontWeight.ExtraBold)
                                            Text("Stock: ${producto.stock} pzs", fontSize = 12.sp, color = if (producto.stock > 0) Color.Gray else Color.Red)
                                        }
                                        Row {
                                            IconButton(onClick = { productToEdit = producto }) { Icon(Icons.Default.Edit, "Editar", tint = Color.Gray) }
                                            IconButton(onClick = { viewModel.solicitarBorrado(producto.id) }) { Icon(Icons.Default.Delete, "Borrar", tint = Color(0xFFFF5252)) }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (productToEdit != null) {
            EditProductDialog(
                producto = productToEdit!!,
                onDismiss = { productToEdit = null },
                onConfirm = { nombre, precio, stock, uri ->
                    val bytes = uri?.let { context.contentResolver.openInputStream(it)?.readBytes() }
                    viewModel.editarProducto(productToEdit!!.id, nombre, precio, stock, bytes)
                    productToEdit = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(producto: SellerProduct, onDismiss: () -> Unit, onConfirm: (String, Double, Int, Uri?) -> Unit) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var stock by remember { mutableStateOf(producto.stock.toString()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }
    val pocketBlue = Color(0xFF3B82F6)
    val inputTextColor = Color(0xFF1F2937)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Editar Producto", fontWeight = FontWeight.Black, color = Color.Black) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF3F4F7)).clickable { launcher.launch("image/*") }, contentAlignment = Alignment.Center) {
                    if (imageUri != null) AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    else AsyncImage(model = producto.fotoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                }
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,
                        unfocusedTextColor = inputTextColor,
                        focusedLabelColor = pocketBlue,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio (MXN)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,
                        unfocusedTextColor = inputTextColor,
                        focusedLabelColor = pocketBlue,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = inputTextColor,
                        unfocusedTextColor = inputTextColor,
                        focusedLabelColor = pocketBlue,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, precio.toDoubleOrNull() ?: 0.0, stock.toIntOrNull() ?: 0, imageUri) }, colors = ButtonDefaults.buttonColors(containerColor = pocketBlue)) {
                Text("GUARDAR", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCELAR", color = Color.Gray) } }
    )
}