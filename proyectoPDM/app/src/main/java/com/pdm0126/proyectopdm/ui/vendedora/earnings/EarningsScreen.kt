package com.pdm0126.proyectopdm.ui.vendedora.earnings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraDestination
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraScaffold
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsScreen(
    viewModel: EarningsViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToAddSale: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    VendedoraScaffold(
        currentDestination = VendedoraDestination.EARNINGS,
        onNavigateToCatalog = onNavigateToCatalog,
        onNavigateToEarnings = { /* Ya estamos aquí */ },
        onNavigateToStats = onNavigateToStats,
        onLogout = onLogout,
        topBarTitle = "Mis Ganancias",
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddSale) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Venta")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Filtros Temporales
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                TimeFilter.values().forEachIndexed { index, filter ->
                    SegmentedButton(
                        selected = uiState.selectedFilter == filter,
                        onClick = { viewModel.onFilterSelected(filter) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = TimeFilter.values().size)
                    ) {
                        Text(filter.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }

            // Resumen de Monto
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Total ${uiState.selectedFilter.name.lowercase()}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "$${String.format("%.2f", uiState.totalAmount)}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Historial de Registros",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (uiState.isLoading && uiState.filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.filteredRecords) { record ->
                        SaleRecordItem(record)
                    }
                }
            }
        }
    }
}

@Composable
fun SaleRecordItem(record: SaleRecord) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                val date = if (record.createdAt != null) {
                    try {
                        ZonedDateTime.parse(record.createdAt).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    } catch (e: Exception) {
                        record.createdAt
                    }
                } else {
                    "Fecha desconocida"
                }
                Text(text = "Venta", style = MaterialTheme.typography.titleMedium)
                Text(text = date ?: "Fecha desconocida", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "+$${String.format("%.2f", record.amount)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
