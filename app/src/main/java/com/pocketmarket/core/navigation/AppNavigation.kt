package com.pocketmarket.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pocketmarket.features.admin.presentation.screens.AdminDashboardScreen
import com.pocketmarket.features.admin.presentation.screens.AdminRequestsScreen
import com.pocketmarket.features.admin.presentation.screens.AdminSellersScreen
import com.pocketmarket.features.auth.presentation.screens.LoginScreen
import com.pocketmarket.features.auth.presentation.screens.RegisterScreen
import com.pocketmarket.features.auth.presentation.screens.RegisterSellerScreen
import com.pocketmarket.features.cart.presentation.screens.CartScreen
import com.pocketmarket.features.cart.presentation.screens.CheckoutScreen
import com.pocketmarket.features.cart.presentation.viewmodels.CartViewModel
import com.pocketmarket.features.products.presentation.screens.FavoritesScreen
import com.pocketmarket.features.products.presentation.screens.HomeScreen
import com.pocketmarket.features.products.presentation.screens.ProductDetailScreen
import com.pocketmarket.features.seller.presentation.screens.AddProductScreen
import com.pocketmarket.features.seller.presentation.screens.SellerDashboardScreen
import com.pocketmarket.features.seller.presentation.screens.SellerMyProductsScreen
import com.pocketmarket.features.seller.presentation.screens.SellerOrdersScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Any,
    onLogoutRequest: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(RegisterRoute) },
                onLoginSuccess = onLoginSuccess
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToRegisterSeller = { navController.navigate(RegisterSellerRoute) },
                onRegisterSuccess = {
                    navController.navigate(LoginRoute) {
                        popUpTo(RegisterRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<RegisterSellerRoute> {
            RegisterSellerScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(RegisterSellerRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onLogout = onLogoutRequest,
                onNavigateToDetail = { id -> navController.navigate(ProductDetailRoute(id.toString())) },
                onNavigateToCart = { navController.navigate(CartRoute) },
                onNavigateToFavorites = { navController.navigate(FavoritesRoute) }
            )
        }


        composable<FavoritesRoute> {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ProductDetailRoute> {
            ProductDetailScreen(onBack = { navController.popBackStack() })
        }

        composable<CartRoute> {
            CartScreen(
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(HomeRoute) { inclusive = true }
                    }
                },
                onNavigateToCheckout = { total -> navController.navigate(CheckoutRoute(total.toFloat())) },
                onLogout = onLogoutRequest
            )
        }

        composable<CheckoutRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<CheckoutRoute>()
            val cartViewModel: CartViewModel = hiltViewModel()
            CheckoutScreen(
                total = args.total.toDouble(),
                onConfirmOrder = {
                    cartViewModel.procederAlPago()
                    navController.navigate(HomeRoute) {
                        popUpTo(HomeRoute) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        val onNavToSellerDashboard = {
            navController.navigate(SellerDashboardRoute) { popUpTo(SellerDashboardRoute) { inclusive = true } }
        }
        val onNavToMyProducts = {
            navController.navigate(SellerMyProductsRoute) { popUpTo(SellerDashboardRoute) { inclusive = false } }
        }
        val onNavToOrders = {
            navController.navigate(SellerOrdersRoute) { popUpTo(SellerDashboardRoute) { inclusive = false } }
        }
        val onNavToAddProduct = {
            navController.navigate(SellerAddProductRoute) { popUpTo(SellerDashboardRoute) { inclusive = false } }
        }

        composable<SellerDashboardRoute> { SellerDashboardScreen(onNavToSellerDashboard, onNavToMyProducts, onNavToOrders, onNavToAddProduct, onLogoutRequest) }
        composable<SellerAddProductRoute> { AddProductScreen(onNavToSellerDashboard, onNavToMyProducts, onNavToOrders, onNavToAddProduct, onLogoutRequest) }
        composable<SellerMyProductsRoute> { SellerMyProductsScreen(onNavToSellerDashboard, onNavToMyProducts, onNavToOrders, onNavToAddProduct, onLogoutRequest) }
        composable<SellerOrdersRoute> { SellerOrdersScreen(onNavToSellerDashboard, onNavToMyProducts, onNavToOrders, onNavToAddProduct, onLogoutRequest) }

        val onNavToAdminDashboard = {
            navController.navigate(AdminDashboardRoute) { popUpTo(AdminDashboardRoute) { inclusive = true } }
        }
        val onNavToAdminRequests = {
            navController.navigate(AdminRequestsRoute) { popUpTo(AdminDashboardRoute) { inclusive = false } }
        }
        val onNavToAdminSellers = {
            navController.navigate(AdminSellersListRoute) { popUpTo(AdminDashboardRoute) { inclusive = false } }
        }

        composable<AdminDashboardRoute> { AdminDashboardScreen(onNavToAdminDashboard, onNavToAdminRequests, onNavToAdminSellers, onLogoutRequest) }
        composable<AdminRequestsRoute> { AdminRequestsScreen(onNavToAdminDashboard, onNavToAdminRequests, onNavToAdminSellers, onLogoutRequest) }
        composable<AdminSellersListRoute> { AdminSellersScreen(onNavToAdminDashboard, onNavToAdminRequests, onNavToAdminSellers, onLogoutRequest) }
    }
}