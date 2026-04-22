package com.pocketmarket.features.admin.data.datasources.remote.mapper

import com.pocketmarket.features.admin.data.datasources.remote.models.*
import com.pocketmarket.features.admin.domain.entities.*

fun AdminSellerDto.toDomain(): AdminSeller {
    val perfil = this.perfilVendedor
    return AdminSeller(
        usuarioId = this.id,
        email = this.email,
        nombreMarca = perfil?.nombreMarca ?: "Sin Marca",
        rfc = perfil?.rfc ?: "N/A",
        telefono = perfil?.telefono ?: "N/A",
        direccion = perfil?.direccion ?: "N/A",
        clabe = perfil?.clabeInterbancaria ?: "N/A",
        ineFrenteUrl = perfil?.ineFrenteUrl ?: "",
        ineTraseraUrl = perfil?.ineTraseraUrl ?: "",
        comprobanteUrl = perfil?.comprobanteDomUrl ?: "",
        estado = perfil?.estado ?: "PENDIENTE"
    )
}

fun AdminStatsDto.toDomain(): AdminStats {
    return AdminStats(
        numerosPrincipales = StatsNumeros(
            totalUsuarios = this.numerosPrincipales.totalUsuarios,
            totalVendedores = this.numerosPrincipales.totalVendedores,
            pedidosCompletados = this.numerosPrincipales.pedidosCompletados,
            pedidosPendientes = this.numerosPrincipales.pedidosPendientes,
            ingresosTotales = this.numerosPrincipales.ingresosTotales
        ),
        statsGraficas = StatsGraficas(
            totalRevenue = this.statsGraficas.totalRevenue,
            activeUsers = this.statsGraficas.activeUsers,
            revenueByMonth = this.statsGraficas.revenueByMonth.map { RevenueMonth(it.label, it.value) },
            userGrowth = this.statsGraficas.userGrowth.map { UserGrowth(it.role, it.count) },
            topSellingProducts = this.statsGraficas.topSellingProducts.map { TopProduct(it.nombre, it.precio) }
        )
    )
}