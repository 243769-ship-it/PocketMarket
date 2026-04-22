package com.pocketmarket.features.auth.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pocketmarket.features.auth.presentation.viewmodels.RegisterSellerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterSellerScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterSellerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var rfc by remember { mutableStateOf("") }
    var clabe by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var ineFrenteBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var ineTraseraBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var comprobanteBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var currentPhotoTarget by remember { mutableStateOf("") }

    val cameraLauncherFrente = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { b -> if (b != null) ineFrenteBitmap = b }
    val cameraLauncherTrasera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { b -> if (b != null) ineTraseraBitmap = b }
    val cameraLauncherComp = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { b -> if (b != null) comprobanteBitmap = b }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            when (currentPhotoTarget) {
                "FRENTE" -> cameraLauncherFrente.launch(null)
                "TRASERA" -> cameraLauncherTrasera.launch(null)
                "COMP" -> cameraLauncherComp.launch(null)
            }
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    fun openCamera(target: String) {
        currentPhotoTarget = target
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            when (target) {
                "FRENTE" -> cameraLauncherFrente.launch(null)
                "TRASERA" -> cameraLauncherTrasera.launch(null)
                "COMP" -> cameraLauncherComp.launch(null)
            }
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is RegisterSellerUiState.Success) {
            Toast.makeText(context, "¡Solicitud enviada! Espera la aprobación.", Toast.LENGTH_LONG).show()
            onNavigateToLogin()
        } else if (uiState is RegisterSellerUiState.Error) {
            Toast.makeText(context, (uiState as RegisterSellerUiState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
        unfocusedContainerColor = Color.White, focusedContainerColor = Color.White
    )

    Scaffold(
        containerColor = Color(0xFFF3F4F7),
        topBar = {
            TopAppBar(
                title = { Text("Registrar Vendedor", fontWeight = FontWeight.Black) },
                navigationIcon = { IconButton(onClick = onNavigateToLogin) { Icon(Icons.Default.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Datos de la Cuenta", fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))

            Text("Datos del Negocio", fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6), modifier = Modifier.padding(top = 8.dp))
            OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Nombre de la Marca") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = rfc, onValueChange = { rfc = it }, label = { Text("RFC") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección Completa") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))
            OutlinedTextField(value = clabe, onValueChange = { clabe = it }, label = { Text("CLABE Interbancaria") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), colors = textFieldColors, shape = RoundedCornerShape(12.dp))

            Text("Documentos (Fotos con Cámara)", fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6), modifier = Modifier.padding(top = 8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CameraBox("INE Frente", ineFrenteBitmap, Modifier.weight(1f)) { openCamera("FRENTE") }
                CameraBox("INE Reverso", ineTraseraBitmap, Modifier.weight(1f)) { openCamera("TRASERA") }
            }
            CameraBox("Comprobante de Domicilio", comprobanteBitmap, Modifier.fillMaxWidth()) { openCamera("COMP") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && marca.isNotBlank() && ineFrenteBitmap != null && ineTraseraBitmap != null && comprobanteBitmap != null) {
                        viewModel.registerSeller(email, password, marca, rfc, clabe, telefono, direccion, ineFrenteBitmap!!, ineTraseraBitmap!!, comprobanteBitmap!!)
                    } else {
                        Toast.makeText(context, "Llena todos los campos y toma las 3 fotos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = uiState !is RegisterSellerUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState is RegisterSellerUiState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Enviar Solicitud", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun CameraBox(label: String, bitmap: Bitmap?, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.height(120.dp).clip(RoundedCornerShape(12.dp)).background(Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Text(label, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}