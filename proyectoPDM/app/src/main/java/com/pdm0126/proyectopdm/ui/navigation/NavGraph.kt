package com.pdm0126.proyectopdm.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.pdm0126.proyectopdm.ui.auth.LoginViewModel
import com.pdm0126.proyectopdm.ui.vendedora.catalog.CatalogScreen
import com.pdm0126.proyectopdm.ui.vendedora.catalog.CatalogViewModel
import com.pdm0126.proyectopdm.ui.vendedora.catalog.ProductDetailScreen
import com.pdm0126.proyectopdm.ui.vendedora.catalog.ProductDetailViewModel
import com.pdm0126.proyectopdm.ui.vendedora.cart.CartScreen
import com.pdm0126.proyectopdm.ui.vendedora.cart.CartViewModel
import com.pdm0126.proyectopdm.ui.admin.dashboard.AdminDashboardScreen
import com.pdm0126.proyectopdm.ui.admin.dashboard.AdminDashboardViewModel
import com.pdm0126.proyectopdm.ui.admin.inventory.InventoryManagementScreen
import com.pdm0126.proyectopdm.ui.admin.inventory.InventoryManagementViewModel
import com.pdm0126.proyectopdm.ui.admin.inventory.AddEditProductScreen
import com.pdm0126.proyectopdm.ui.admin.inventory.AddEditProductViewModel
import com.pdm0126.proyectopdm.ui.admin.users.UserManagementScreen
import com.pdm0126.proyectopdm.ui.admin.users.UserManagementViewModel
import com.pdm0126.proyectopdm.ui.vendedora.sales.AddSaleScreen
import com.pdm0126.proyectopdm.ui.vendedora.sales.AddSaleViewModel
import com.pdm0126.proyectopdm.ui.vendedora.earnings.EarningsScreen
import com.pdm0126.proyectopdm.ui.vendedora.earnings.EarningsViewModel
import com.pdm0126.proyectopdm.ui.vendedora.stats.StatsScreen
import com.pdm0126.proyectopdm.ui.vendedora.stats.StatsViewModel

@Composable
fun NavGraph(
    backStack: SnapshotStateList<Any>,
    loginViewModel: LoginViewModel,
    catalogViewModel: CatalogViewModel,
    adminDashboardViewModel: AdminDashboardViewModel,
    inventoryViewModel: InventoryManagementViewModel,
    userManagementViewModel: UserManagementViewModel,
    earningsViewModel: EarningsViewModel,
    statsViewModel: StatsViewModel,
    addEditViewModelProvider: (String?) -> AddEditProductViewModel,
    addSaleViewModel: AddSaleViewModel,
    productDetailViewModelProvider: (String) -> ProductDetailViewModel,
    cartViewModel: CartViewModel
) {
    fun navigateTo(key: Any) {
        if (backStack.lastOrNull() != key) {
            if (backStack.contains(key)) {
                backStack.remove(key)
            }
            backStack.add(key)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { 
            if (backStack.size > 1) {
                backStack.removeAt(backStack.size - 1)
            }
        },
        entryProvider = { key ->
            when (key) {
                is NavKey.VendedoraCatalog -> NavEntry(key) {
                    CatalogScreen(
                        viewModel = catalogViewModel,
                        onProductClick = { productId ->
                            navigateTo(NavKey.ProductDetail(productId))
                        },
                        onNavigateToCart = { navigateTo(NavKey.Cart) },
                        onNavigateToEarnings = { navigateTo(NavKey.Earnings) },
                        onNavigateToStats = { navigateTo(NavKey.Stats) },
                        onLogout = {
                            loginViewModel.signOut()
                        }
                    )
                }
                is NavKey.AdminDashboard -> NavEntry(key) {
                    AdminDashboardScreen(
                        viewModel = adminDashboardViewModel,
                        onNavigateToInventory = { navigateTo(NavKey.InventoryManagement) },
                        onNavigateToUsers = { navigateTo(NavKey.UserManagement) },
                        onLogout = {
                            loginViewModel.signOut()
                        }
                    )
                }
                is NavKey.InventoryManagement -> NavEntry(key) {
                    InventoryManagementScreen(
                        viewModel = inventoryViewModel,
                        onAddProduct = { navigateTo(NavKey.AddEditProduct()) },
                        onEditProduct = { id -> navigateTo(NavKey.AddEditProduct(id)) },
                        onBack = { backStack.removeLast() }
                    )
                }
                is NavKey.AddEditProduct -> NavEntry(key) {
                    AddEditProductScreen(
                        viewModel = addEditViewModelProvider(key.productId),
                        onBack = { backStack.removeLast() }
                    )
                }
                is NavKey.UserManagement -> NavEntry(key) {
                    UserManagementScreen(
                        viewModel = userManagementViewModel,
                        onAddUser = { navigateTo(NavKey.AddUser) },
                        onBack = { backStack.removeLast() }
                    )
                }
                is NavKey.AddUser -> NavEntry(key) {
                    val appContext = androidx.compose.ui.platform.LocalContext.current.applicationContext
                    com.pdm0126.proyectopdm.ui.admin.users.AddUserScreen(
                        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    val profileRepo = com.pdm0126.proyectopdm.data.repository.ProfileRepositoryImpl(appContext)
                                    return com.pdm0126.proyectopdm.ui.admin.users.AddUserViewModel(profileRepo) as T
                                }
                            }
                        ),
                        onBack = { backStack.removeLast() },
                        onSuccess = { backStack.removeLast() }
                    )
                }
                is NavKey.AddSale -> NavEntry(key) {
                    AddSaleScreen(
                        viewModel = addSaleViewModel,
                        onBack = { backStack.removeLast() }
                    )
                }
                is NavKey.Earnings -> NavEntry(key) {
                    EarningsScreen(
                        viewModel = earningsViewModel,
                        onNavigateToCatalog = { navigateTo(NavKey.VendedoraCatalog) },
                        onNavigateToStats = { navigateTo(NavKey.Stats) },
                        onNavigateToAddSale = { navigateTo(NavKey.AddSale) },
                        onLogout = {
                            loginViewModel.signOut()
                        }
                    )
                }
                is NavKey.Stats -> NavEntry(key) {
                    StatsScreen(
                        viewModel = statsViewModel,
                        onNavigateToCatalog = { navigateTo(NavKey.VendedoraCatalog) },
                        onNavigateToEarnings = { navigateTo(NavKey.Earnings) },
                        onLogout = {
                            loginViewModel.signOut()
                        }
                    )
                }
                is NavKey.ProductDetail -> NavEntry(key) {
                    ProductDetailScreen(
                        viewModel = productDetailViewModelProvider(key.productId),
                        onBack = { backStack.removeLast() },
                        onAddToCart = { product ->
                            cartViewModel.addToCart(product)
                            backStack.removeLast()
                        }
                    )
                }
                is NavKey.Cart -> NavEntry(key) {
                    CartScreen(
                        viewModel = cartViewModel,
                        onBack = { backStack.removeLast() }
                    )
                }
                else -> NavEntry(key) {
                    Text("Ruta no encontrada")
                }
            }
        }
    )
}
