package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getLocalProducts(): Flow<List<Product>>
    suspend fun syncProducts(): DataResult<Unit>
    suspend fun getProductsOnce(): List<Product>

    suspend fun createProduct(product: Product): DataResult<Unit>
    suspend fun updateProduct(product: Product): DataResult<Unit>
    suspend fun disableProduct(productId: String): DataResult<Unit>
}