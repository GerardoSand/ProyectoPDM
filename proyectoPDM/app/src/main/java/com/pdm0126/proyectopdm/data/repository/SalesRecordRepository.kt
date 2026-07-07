package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.remote.SaleRecordDto
import com.pdm0126.proyectopdm.data.remote.SalesRecordApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SalesRecordRepository(context: Context) {
    private val api = SalesRecordApi()
    private val dao = DatabaseProvider.getDatabase(context).salesRecordDao()

    fun getSalesRecords(userId: String): Flow<List<SaleRecord>> {
        return dao.getSalesRecords(userId).map { entities ->
            entities.toDomainList()
        }
    }

    suspend fun syncSalesRecords(userId: String) {
        try {
            val remoteRecords = api.getSalesRecords(userId)

            dao.clearSalesRecords(userId)
            dao.insertSalesRecords(remoteRecords.toEntityList())
        } catch (e: Exception) {
        }
    }

    suspend fun saveSaleRecord(userId: String, amount: Double) {
        val newRecordDto = SaleRecordDto(
            id = java.util.UUID.randomUUID().toString(), // logica de id no se si vamos a utilizar solo un random
            userId = userId,
            amount = amount,
            createdAt = ""
        )
        
        try {
            api.insertSaleRecord(newRecordDto)
            syncSalesRecords(userId)
        } catch (e: Exception) {
        }
    }
}