package com.pdm0126.proyectopdm.ui.vendedora.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraDestination
import com.pdm0126.proyectopdm.ui.vendedora.components.VendedoraScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    onNavigateToCatalog: () -> Unit,
    onNavigateToEarnings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    VendedoraScaffold(
        currentDestination = VendedoraDestination.STATS,
        onNavigateToCatalog = onNavigateToCatalog,
        onNavigateToEarnings = onNavigateToEarnings,
        onNavigateToStats = { /* Ya estamos aquí */ },
        onLogout = onLogout,
        topBarTitle = "Mi Rendimiento"
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjetas de Resumen
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    title = "Este Mes",
                    amount = uiState.totalThisMonth,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
                StatCard(
                    title = "Mes Pasado",
                    amount = uiState.totalLastMonth,
                    modifier = Modifier.weight(1f),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Comparativa de Crecimiento
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Crecimiento mensual", style = MaterialTheme.typography.titleMedium)
                    val color = if (uiState.growthPercentage >= 0) Color(0xFF4CAF50) else Color.Red
                    Text(
                        text = "${if (uiState.growthPercentage >= 0) "+" else ""}${String.format("%.1f", uiState.growthPercentage)}%",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Text(text = "Comparado con el mes anterior", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Gráfico Simple de Barras (Últimos 7 días)
            Text(text = "Ventas - Últimos 7 días", style = MaterialTheme.typography.titleLarge)
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (uiState.last7DaysSales.isEmpty()) {
                    Text("Sin datos recientes")
                } else {
                    val maxAmount = (uiState.last7DaysSales.maxOfOrNull { it.amount } ?: 1.0).coerceAtLeast(1.0)
                    
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        uiState.last7DaysSales.forEach { day ->
                            val barHeight = (day.amount / maxAmount).toFloat()
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .fillMaxHeight(barHeight.coerceAtLeast(0.05f))
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = day.dayName, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, amount: Double, modifier: Modifier, containerColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "$${String.format("%.0f", amount)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
