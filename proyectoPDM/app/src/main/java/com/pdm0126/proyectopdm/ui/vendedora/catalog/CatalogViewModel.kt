package com.pdm0126.proyectopdm.ui.vendedora.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CatalogUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList()
)

class CatalogViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<CatalogUiState> = combine(
        productRepository.getLocalProducts(),
        _searchQuery,
        _selectedCategory,
        _isLoading
    ) { products, query, category, loading ->
        
        val filteredProducts = products.filter { product ->
            val matchesSearch = product.name.contains(query, ignoreCase = true) || 
                               product.brand.contains(query, ignoreCase = true)
            val matchesCategory = category == null || product.category == category
            matchesSearch && matchesCategory
        }

        val allCategories = products.map { it.category }.distinct()

        CatalogUiState(
            products = filteredProducts,
            isLoading = loading,
            searchQuery = query,
            selectedCategory = category,
            categories = allCategories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CatalogUiState(isLoading = true)
    )

    init {
        syncProducts()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String?) {
        _selectedCategory.value = category
    }

    fun syncProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                productRepository.syncProducts()
            } catch (e: Exception) {
                // Manejar error de sincronización
            } finally {
                _isLoading.value = false
            }
        }
    }
}
