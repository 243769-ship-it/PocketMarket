package com.pocketmarket.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object RegisterSellerRoute

@Serializable
object HomeRoute

@Serializable
object CartRoute

@Serializable
data class ProductDetailRoute(val id: String)

@Serializable
data class CheckoutRoute(val total: Float)

@Serializable
object SellerDashboardRoute

@Serializable
object SellerAddProductRoute

@Serializable
object SellerMyProductsRoute

@Serializable
object SellerOrdersRoute

@Serializable
object AdminDashboardRoute

@Serializable
object AdminRequestsRoute

@Serializable
object AdminSellersListRoute

@Serializable
object FavoritesRoute