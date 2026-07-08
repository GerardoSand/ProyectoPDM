package com.pdm0126.proyectopdm.ui.vendedora.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import android.util.Log

enum class TimeFilter { DIARIO, SEMANAL, MENSUAL }

data class EarningsUiState(
    val records: List<SaleRecord> = emptyList(),
    val filteredRecords: List<SaleRecord> = emptyList(),
    val totalAmount: Double = 0.0,
    val selectedFilter: TimeFilter = TimeFilter.DIARIO,
    val isLoading: Boolean = false
)

class EarningsViewModel(
    private val salesRecordRepository: SalesRecordRepository,
    private val userId: String
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(TimeFilter.DIARIO)
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<EarningsUiState> = combine(
        salesRecordRepository.getSalesRecords(userId),
        _selectedFilter,
        _isLoading
    ) { records, filter, loading ->
        val now = ZonedDateTime.now()
        
        val filtered = records.filter { record ->
            if (record.createdAt.isNullOrBlank()) return@filter false
            try {
                val recordDate = ZonedDateTime.parse(record.createdAt)
                when (filter) {
                    TimeFilter.DIARIO -> recordDate.toLocalDate() == now.toLocalDate()
                    TimeFilter.SEMANAL -> ChronoUnit.WEEKS.between(recordDate, now) == 0L
                    TimeFilter.MENSUAL -> recordDate.month == now.month && recordDate.year == now.year
                }
            } catch (e: Exception) {
                Log.e("EarningsVM", "Error parsing date: ${record.createdAt}", e)
                false
            }
        }

        EarningsUiState(
            records = records,
            filteredRecords = filtered,
            totalAmount = filtered.sumOf { it.amount },
            selectedFilter = filter,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EarningsUiState(isLoading = true)
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            salesRecordRepository.syncSalesRecords(userId)
            _isLoading.value = false
        }
    }

    fun onFilterSelected(filter: TimeFilter) {
        _selectedFilter.value = filter
    }
}
