package com.pdm0126.proyectopdm.ui.vendedora.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

enum class VendedoraDestination {
    CATALOG, EARNINGS, STATS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendedoraScaffold(
    currentDestination: VendedoraDestination,
    onNavigateToCatalog: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onNavigateToStats: () -> Unit,
    onLogout: () -> Unit,
    topBarTitle: String = "Sercom",
    floatingActionButton: @Composable () -> Unit = {},
    topBarExtraContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Menú Vendedora",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                NavigationDrawerItem(
                    label = { Text("Catálogo") },
                    selected = currentDestination == VendedoraDestination.CATALOG,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigateToCatalog()
                        }
                    },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Mis Ganancias") },
                    selected = currentDestination == VendedoraDestination.EARNINGS,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigateToEarnings()
                        }
                    },
                    icon = { Icon(Icons.Default.Payments, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Mi Rendimiento") },
                    selected = currentDestination == VendedoraDestination.STATS,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onNavigateToStats()
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null) }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onLogout()
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text(topBarTitle) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                    topBarExtraContent()
                }
            },
            floatingActionButton = floatingActionButton
        ) { padding ->
            content(padding)
        }
    }
}
