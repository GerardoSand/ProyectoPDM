package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.remote.ProductApi
import com.pdm0126.proyectopdm.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepository(context: Context) {

    private val api = ProductApi()
    private val productDao = DatabaseProvider
        .getDatabase(context)
        .productDao()

    fun getLocalProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts()
            .map { entities -> entities.toDomainList() }
    }

    suspend fun syncProducts() {
        val remoteProducts = api.getProducts()
        val entities = remoteProducts.toEntityList()

        productDao.clearProducts()
        productDao.insertProducts(entities)
    }

    suspend fun getProductsOnce(): List<Product> {
        return productDao.getAllProductsOnce().toDomainList()
    }
}