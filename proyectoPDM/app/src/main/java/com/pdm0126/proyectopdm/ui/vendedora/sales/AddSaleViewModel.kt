package com.pdm0126.proyectopdm.ui.vendedora.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddSaleUiState(
    val amount: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

class AddSaleViewModel(
    private val salesRecordRepository: SalesRecordRepository,
    private val userId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddSaleUiState())
    val uiState: StateFlow<AddSaleUiState> = _uiState.asStateFlow()

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(amount = value) }
    }

    fun saveSale() {
        val amountValue = _uiState.value.amount.toDoubleOrNull() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            salesRecordRepository.saveSaleRecord(userId, amountValue)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}
