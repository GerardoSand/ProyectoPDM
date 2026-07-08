package com.pdm0126.proyectopdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< HEAD
import com.pdm0126.proyectopdm.ui.theme.ProyectoPDMTheme
=======
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdm0126.proyectopdm.data.repository.*
import com.pdm0126.proyectopdm.ui.auth.AuthState
import com.pdm0126.proyectopdm.ui.auth.LoginScreen
import com.pdm0126.proyectopdm.ui.auth.LoginViewModel
import com.pdm0126.proyectopdm.ui.navigation.NavGraph
import com.pdm0126.proyectopdm.ui.navigation.NavKey
import com.pdm0126.proyectopdm.ui.theme.ProyectoPDMTheme
import com.pdm0126.proyectopdm.ui.vendedora.catalog.CatalogViewModel
import com.pdm0126.proyectopdm.ui.admin.dashboard.AdminDashboardViewModel
import com.pdm0126.proyectopdm.ui.admin.inventory.InventoryManagementViewModel
import com.pdm0126.proyectopdm.ui.admin.inventory.AddEditProductViewModel
import com.pdm0126.proyectopdm.ui.admin.users.UserManagementViewModel
import com.pdm0126.proyectopdm.ui.vendedora.sales.AddSaleViewModel
import com.pdm0126.proyectopdm.ui.vendedora.earnings.EarningsViewModel
import com.pdm0126.proyectopdm.ui.vendedora.stats.StatsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdm0126.proyectopdm.ui.vendedora.catalog.ProductDetailViewModel
import com.pdm0126.proyectopdm.ui.vendedora.cart.CartViewModel
import io.github.jan.supabase.auth.auth
>>>>>>> 8b91bae (ui)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
<<<<<<< HEAD
        setContent {
            ProyectoPDMTheme {
=======

        val productRepository = ProductRepositoryImpl(applicationContext)
        val profileRepository = ProfileRepositoryImpl(applicationContext)
        val salesRecordRepository = SalesRecordRepositoryImpl(applicationContext)

        setContent {
            ProyectoPDMTheme {
                val loginViewModel: LoginViewModel = viewModel()
                val authState by loginViewModel.authState.collectAsState()

                when (val state = authState) {
                    is AuthState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is AuthState.Unauthenticated -> {
                        LoginScreen(viewModel = loginViewModel)
                    }
                    is AuthState.Authenticated -> {
                        val currentUserId = com.pdm0126.proyectopdm.data.remote.ApiClient.supabase.auth.currentUserOrNull()?.id ?: "b939212c-2377-47f8-b760-9a8a04fa1f45"
                        
                        val backStack = remember(state.role) { 
                            val startDest = if (state.role.trim().lowercase() == "admin") NavKey.AdminDashboard else NavKey.VendedoraCatalog
                            mutableStateListOf<Any>(startDest) 
                        }

                        val catalogViewModel: CatalogViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return CatalogViewModel(productRepository) as T
                                }
                            }
                        )

                        val adminDashboardViewModel: AdminDashboardViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return AdminDashboardViewModel(profileRepository, salesRecordRepository) as T
                                }
                            }
                        )

                        val inventoryViewModel: InventoryManagementViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return InventoryManagementViewModel(productRepository) as T
                                }
                            }
                        )

                        val userManagementViewModel: UserManagementViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return UserManagementViewModel(profileRepository) as T
                                }
                            }
                        )

                        val addSaleViewModel: AddSaleViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return AddSaleViewModel(salesRecordRepository, currentUserId) as T
                                }
                            }
                        )

                        val earningsViewModel: EarningsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return EarningsViewModel(salesRecordRepository, currentUserId) as T
                                }
                            }
                        )

                        val statsViewModel: StatsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    return StatsViewModel(salesRecordRepository, currentUserId) as T
                                }
                            }
                        )

                        NavGraph(
                            backStack = backStack,
                            loginViewModel = loginViewModel,
                            catalogViewModel = catalogViewModel,
                            adminDashboardViewModel = adminDashboardViewModel,
                            inventoryViewModel = inventoryViewModel,
                            userManagementViewModel = userManagementViewModel,
                            earningsViewModel = earningsViewModel,
                            statsViewModel = statsViewModel,
                            addEditViewModelProvider = { id ->
                                ViewModelProvider(this, object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        return AddEditProductViewModel(productRepository, id) as T
                                    }
                                })[AddEditProductViewModel::class.java]
                            },
                            addSaleViewModel = addSaleViewModel,
                            productDetailViewModelProvider = { id ->
                                ViewModelProvider(this, object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        return ProductDetailViewModel(productRepository, id) as T
                                    }
                                })[ProductDetailViewModel::class.java]
                            },
                            cartViewModel = viewModel(
                                factory = object : ViewModelProvider.Factory {
                                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                        return CartViewModel(salesRecordRepository, currentUserId) as T
                                    }
                                }
                            )
                        )
                    }
                }
>>>>>>> 8b91bae (ui)
            }
        }
    }
}
