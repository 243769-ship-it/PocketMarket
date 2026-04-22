package com.pocketmarket.features.products.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketmarket.features.products.presentation.components.ProductCard
import com.pocketmarket.features.products.presentation.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    var selectedTab by remember { mutableStateOf("home") }
    val primaryColor = Color(0xFF1E3A8A)
    val backgroundColor = Color(0xFFEBEBEB)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = selectedTab == "home",
                    onClick = {
                        selectedTab = "home"
                        viewModel.onSearchQueryChange("")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Tiendas") },
                    selected = selectedTab == "tiendas",
                    onClick = {
                        selectedTab = "tiendas"
                        viewModel.onSearchQueryChange("")
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    label = { Text("Carrito") },
                    selected = false,
                    onClick = onNavigateToCart
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Salir") },
                    selected = false,
                    onClick = onLogout
                )
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryColor)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.onSearchQueryChange(it)
                            if (it.isNotBlank()) selectedTab = "home"
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .background(Color.White, RoundedCornerShape(25.dp)),
                        placeholder = { Text("Buscar en PocketMarket...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(25.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = onNavigateToFavorites,
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, CircleShape)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favoritos", tint = Color.Red)
                    }
                }
            }

            if (selectedTab == "home") {
                if (searchQuery.isBlank()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "GRAN VENTA DE LIQUIDACIÓN",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                when (val state = uiState) {
                    is HomeUiState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = primaryColor)
                            }
                        }
                    }
                    is HomeUiState.Error -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    is HomeUiState.Success -> {
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = if (searchQuery.isNotBlank()) "Resultados de búsqueda" else "Descubre lo que tenemos para ti",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        items(state.productos) { producto ->
                            val isFavorite = favoriteIds.contains(producto.id)
                            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                                ProductCard(
                                    producto = producto,
                                    isFavorite = isFavorite,
                                    onToggleFavorite = { viewModel.toggleFavorite(producto, isFavorite) },
                                    onClick = { onNavigateToDetail(producto.id) }
                                )
                            }
                        }
                    }
                }
            } else {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = "Tiendas Oficiales",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                when (val state = uiState) {
                    is HomeUiState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = primaryColor)
                            }
                        }
                    }
                    is HomeUiState.Error -> {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    is HomeUiState.Success -> {
                        if (state.tiendas.isEmpty()) {
                            item(span = { GridItemSpan(2) }) {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    Text(text = "No hay tiendas disponibles", color = Color.Gray)
                                }
                            }
                        } else {
                            items(state.tiendas) { tienda ->
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {
                                            viewModel.onSearchQueryChange(tienda.nombreMarca)
                                            selectedTab = "home"
                                        },
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF3F4F7)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor, modifier = Modifier.size(32.dp))
                                        }
                                        Spacer(Modifier.height(8.dp))
                                        Text(
                                            text = tienda.nombreMarca,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            fontSize = 14.sp
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