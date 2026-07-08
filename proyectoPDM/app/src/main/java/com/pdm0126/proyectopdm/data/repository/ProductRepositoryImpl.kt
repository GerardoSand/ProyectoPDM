package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.model.Product
import com.pdm0126.proyectopdm.data.remote.ProductApi
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(context: Context) : ProductRepository {

    private val api = ProductApi()
    private val productDao = DatabaseProvider
        .getDatabase(context)
        .productDao()

    override fun getLocalProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts()
            .map { entities -> entities.toDomainList() }
    }

    override suspend fun syncProducts(): DataResult<Unit> {
        return try {
            val remoteProducts = api.getProducts()
            val entities = remoteProducts.toEntityList()

            productDao.clearProducts()
            productDao.insertProducts(entities)

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al sincronizar productos")
        }
    }

    override suspend fun getProductsOnce(): List<Product> {
        return productDao.getAllProductsOnce().toDomainList()
    }
}