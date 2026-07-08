package com.pdm0126.proyectopdm.ui.vendedora.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraDestination
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onProductClick: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onNavigateToStats: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    VendedoraScaffold(
        currentDestination = VendedoraDestination.CATALOG,
        onNavigateToCatalog = { /* Ya estamos aquí */ },
        onNavigateToEarnings = onNavigateToEarnings,
        onNavigateToStats = onNavigateToStats,
        onLogout = onLogout,
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCart) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Ver Carrito")
            }
        },
        topBarExtraContent = {
            Column {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text("Buscar marca o producto...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) { }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedCategory == null,
                            onClick = { viewModel.onCategorySelect(null) },
                            label = { Text("Todos") }
                        )
                    }
                    items(uiState.categories) { category ->
                        FilterChip(
                            selected = uiState.selectedCategory == category,
                            onClick = { viewModel.onCategorySelect(category) },
                            label = { Text(category) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading && uiState.products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(uiState.products) { product ->
                    ProductItem(product = product, onClick = { onProductClick(product.id) })
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(onClick = onClick) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = product.brand, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Contado: $${product.priceContado}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Cuotas: $${product.priceCuotas}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
