package com.pocketmarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.pocketmarket.core.navigation.AppNavigation
import com.pocketmarket.core.navigation.LoginRoute
import com.pocketmarket.core.ui.theme.PocketMarketTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketMarketTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val startDest by viewModel.startDestination.collectAsState()
                    val navController = rememberNavController()

                    if (startDest != null) {
                        AppNavigation(
                            navController = navController,
                            startDestination = startDest!!,
                            onLogoutRequest = {
                                viewModel.cerrarSesion()
                                navController.navigate(LoginRoute) { popUpTo(0) }
                            },
                            onLoginSuccess = {
                                lifecycleScope.launch {
                                    val destino = viewModel.obtenerRutaPostLogin()
                                    navController.navigate(destino) {
                                        popUpTo(LoginRoute) { inclusive = true }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}