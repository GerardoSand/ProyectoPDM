package com.pdm0126.proyectopdm.ui.vendedora.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class PaymentMethod { CONTADO, CUOTAS }

data class CartUiState(
    val items: List<Product> = emptyList(),
    val isCheckingOut: Boolean = false,
    val checkoutSuccess: Boolean = false,
    val checkoutError: String? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.CONTADO
) {
    val totalAmount: Double
        get() = if (paymentMethod == PaymentMethod.CONTADO) {
            items.sumOf { it.priceContado }
        } else {
            items.sumOf { it.priceCuotas }
        }
}

class CartViewModel(
    private val salesRecordRepository: SalesRecordRepository,
    private val userId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(product: Product) {
        _uiState.update { it.copy(items = it.items + product, checkoutSuccess = false, checkoutError = null) }
    }

    fun removeFromCart(product: Product) {
        _uiState.update { state -> 
            val newItems = state.items.toMutableList()
            newItems.remove(product)
            state.copy(items = newItems)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(items = emptyList(), checkoutSuccess = false, checkoutError = null) }
    }

    fun updatePaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(paymentMethod = method) }
    }
    
    fun resetCheckoutState() {
        _uiState.update { it.copy(checkoutSuccess = false, checkoutError = null) }
    }

    fun checkout() {
        val total = _uiState.value.totalAmount
        if (total <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true, checkoutError = null) }
            try {
                salesRecordRepository.saveSaleRecord(userId, total)
                _uiState.update { it.copy(isCheckingOut = false, checkoutSuccess = true, items = emptyList()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isCheckingOut = false, checkoutError = e.message ?: "Error al procesar compra") }
            }
        }
    }
}
