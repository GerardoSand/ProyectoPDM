package com.pdm0126.proyectopdm.ui.admin.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class AddEditProductUiState(
    val id: String = "",
    val name: String = "",
    val brand: String = "",
    val priceContado: String = "",
    val priceCuotas: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val isEditing: Boolean = false
)

class AddEditProductViewModel(
    private val productRepository: ProductRepository,
    private val productId: String? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditProductUiState())
    val uiState: StateFlow<AddEditProductUiState> = _uiState.asStateFlow()

    init {
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isEditing = true) }
            val product = productRepository.getProductById(id)
            if (product != null) {
                _uiState.update {
                    it.copy(
                        id = product.id,
                        name = product.name,
                        brand = product.brand,
                        priceContado = product.priceContado.toString(),
                        priceCuotas = product.priceCuotas.toString(),
                        category = product.category,
                        imageUrl = product.imageUrl ?: "",
                        isActive = product.isActive,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onNameChange(value: String) { _uiState.update { it.copy(name = value) } }
    fun onBrandChange(value: String) { _uiState.update { it.copy(brand = value) } }
    fun onPriceContadoChange(value: String) { _uiState.update { it.copy(priceContado = value) } }
    fun onPriceCuotasChange(value: String) { _uiState.update { it.copy(priceCuotas = value) } }
    fun onCategoryChange(value: String) { _uiState.update { it.copy(category = value) } }
    fun onImageUrlChange(value: String) { _uiState.update { it.copy(imageUrl = value) } }

    fun saveProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val product = Product(
                id = if (state.isEditing) state.id else UUID.randomUUID().toString(),
                name = state.name,
                brand = state.brand,
                priceContado = state.priceContado.toDoubleOrNull() ?: 0.0,
                priceCuotas = state.priceCuotas.toDoubleOrNull() ?: 0.0,
                category = state.category,
                imageUrl = state.imageUrl.ifBlank { null },
                isActive = state.isActive,
                createdAt = null
            )

            if (state.isEditing) {
                productRepository.updateProduct(product)
            } else {
                productRepository.addProduct(product)
            }
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}
