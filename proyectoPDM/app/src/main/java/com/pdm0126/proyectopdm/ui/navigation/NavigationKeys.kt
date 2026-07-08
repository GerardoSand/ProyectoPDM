package com.pdm0126.proyectopdm.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavKey {
    @Serializable
    data object Login : NavKey
    
    @Serializable
    data object VendedoraCatalog : NavKey
    
    @Serializable
    data object Cart : NavKey
    
    @Serializable
    data class ProductDetail(val productId: String) : NavKey
    
    @Serializable
    data object Earnings : NavKey
    
    @Serializable
    data object Stats : NavKey
    
    @Serializable
    data object AdminDashboard : NavKey

    @Serializable
    data object InventoryManagement : NavKey

    @Serializable
    data class AddEditProduct(val productId: String? = null) : NavKey

    @Serializable
    data object UserManagement : NavKey

    @Serializable
    data object AddUser : NavKey

    @Serializable
    data object AddSale : NavKey
}
