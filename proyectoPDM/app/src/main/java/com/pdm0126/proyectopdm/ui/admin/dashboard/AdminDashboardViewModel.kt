package com.pdm0126.proyectopdm.ui.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.repository.ProfileRepository
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class VendedoraSales(
    val name: String,
    val totalAmount: Double
)

data class AdminDashboardUiState(
    val topVendedoras: List<VendedoraSales> = emptyList(),
    val isLoading: Boolean = false
)

class AdminDashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val salesRecordRepository: SalesRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadTopVendedoras()
    }

    fun loadTopVendedoras() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Sincronizamos perfiles
                profileRepository.syncProfiles()
                
                val profiles = profileRepository.getProfiles().first()
                val sales = salesRecordRepository.getAllSalesRecords()

                // Filtrar ventas del último mes
                val now = ZonedDateTime.now()
                val oneMonthAgo = now.minusMonths(1)
                
                // Agrupar ventas por usuario
                val salesByUser = sales.filter { sale ->
                    if (sale.createdAt.isNullOrBlank()) return@filter false
                    try {
                        val saleDate = ZonedDateTime.parse(sale.createdAt)
                        saleDate.isAfter(oneMonthAgo)
                    } catch (e: Exception) {
                        false
                    }
                }.groupBy { it.userId }
                
                val topList = profiles.filter { it.role == "vendedora" }
                    .map { profile ->
                        val total = salesByUser[profile.id]?.sumOf { it.amount } ?: 0.0
                        VendedoraSales(profile.fullName ?: "Desconocida", total)
                    }
                    .sortedByDescending { it.totalAmount }

                _uiState.update { it.copy(topVendedoras = topList, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
