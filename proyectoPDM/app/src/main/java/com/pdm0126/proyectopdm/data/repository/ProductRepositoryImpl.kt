package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomain
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toDto
<<<<<<< HEAD
=======
import com.pdm0126.proyectopdm.data.mappers.toEntity
>>>>>>> 8b91bae (ui)
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

<<<<<<< HEAD
    override suspend fun createProduct(product: Product): DataResult<Unit> {
        return try {
            api.createProduct(product.toDto())
            syncProducts()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al crear producto")
        }
    }

    override suspend fun updateProduct(product: Product): DataResult<Unit> {
        return try {
            api.updateProduct(product.id, product.toDto())
            syncProducts()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al actualizar producto")
        }
    }

    override suspend fun disableProduct(productId: String): DataResult<Unit> {
        return try {
            api.disableProduct(productId)
            syncProducts()
            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al desactivar producto")
=======
    override suspend fun getProductById(id: String): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun addProduct(product: Product) {
        try {
            api.insertProduct(product.toDto())
            productDao.insertProduct(product.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateProduct(product: Product) {
        try {
            api.updateProduct(product.toDto())
            productDao.insertProduct(product.toEntity())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteProduct(id: String) {
        try {
            api.deleteProduct(id)
            productDao.deleteProduct(id)
        } catch (e: Exception) {
            e.printStackTrace()
>>>>>>> 8b91bae (ui)
        }
    }
}