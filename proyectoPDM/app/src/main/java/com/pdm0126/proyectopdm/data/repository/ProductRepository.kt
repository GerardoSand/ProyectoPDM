package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getLocalProducts(): Flow<List<Product>>
    suspend fun syncProducts()
    suspend fun getProductsOnce(): List<Product>
}