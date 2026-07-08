package com.pdm0126.proyectopdm.ui.vendedora.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import android.util.Log

data class DailyStat(val dayName: String, val amount: Double)

data class StatsUiState(
    val totalThisMonth: Double = 0.0,
    val totalLastMonth: Double = 0.0,
    val growthPercentage: Double = 0.0,
    val last7DaysSales: List<DailyStat> = emptyList(),
    val isLoading: Boolean = false
)

class StatsViewModel(
    private val salesRecordRepository: SalesRecordRepository,
    private val userId: String
) : ViewModel() {

    init {
        viewModelScope.launch {
            salesRecordRepository.syncSalesRecords(userId)
        }
    }

    val uiState: StateFlow<StatsUiState> = salesRecordRepository.getSalesRecords(userId)
        .map { records ->
            val now = ZonedDateTime.now()
            val thisMonth = now.month
            val lastMonth = now.minusMonths(1).month
            val year = now.year

            val currentMonthTotal = records.filter {
                try {
                    val d = ZonedDateTime.parse(it.createdAt)
                    d.month == thisMonth && d.year == year
                } catch (e: Exception) { 
                    Log.e("StatsVM", "Error parsing date: ${it.createdAt}", e)
                    false 
                }
            }.sumOf { it.amount }

            val lastMonthTotal = records.filter {
                try {
                    val d = ZonedDateTime.parse(it.createdAt)
                    d.month == lastMonth && d.year == (if (thisMonth.value == 1) year - 1 else year)
                } catch (e: Exception) { 
                    Log.e("StatsVM", "Error parsing date: ${it.createdAt}", e)
                    false 
                }
            }.sumOf { it.amount }

            val growth = if (lastMonthTotal > 0) {
                ((currentMonthTotal - lastMonthTotal) / lastMonthTotal) * 100
            } else {
                if (currentMonthTotal > 0) 100.0 else 0.0
            }

            // Últimos 7 días para el gráfico
            val formatter = DateTimeFormatter.ofPattern("EEE")
            val last7Days = (0..6).reversed().map { daysAgo ->
                val date = now.minusDays(daysAgo.toLong())
                val dayName = date.format(formatter)
                val dayAmount = records.filter {
                    try {
                        ZonedDateTime.parse(it.createdAt).toLocalDate() == date.toLocalDate()
                    } catch (e: Exception) { 
                        Log.e("StatsVM", "Error parsing date: ${it.createdAt}", e)
                        false 
                    }
                }.sumOf { it.amount }
                DailyStat(dayName, dayAmount)
            }

            StatsUiState(
                totalThisMonth = currentMonthTotal,
                totalLastMonth = lastMonthTotal,
                growthPercentage = growth,
                last7DaysSales = last7Days,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StatsUiState(isLoading = true)
        )
}
