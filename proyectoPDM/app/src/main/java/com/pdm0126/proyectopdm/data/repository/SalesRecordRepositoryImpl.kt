package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.remote.SaleRecordDto
import com.pdm0126.proyectopdm.data.remote.SalesRecordApi
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SalesRecordRepositoryImpl(context: Context) : SalesRecordRepository{
    private val api = SalesRecordApi()
    private val dao = DatabaseProvider.getDatabase(context).salesRecordDao()

    override fun getSalesRecords(userId: String): Flow<List<SaleRecord>> {
        return dao.getSalesRecords(userId).map { it.toDomainList() }
    }

    override suspend fun syncSalesRecords(userId: String): DataResult<Unit> {
        return try {
            val remoteRecords = api.getSalesRecords(userId)
            dao.clearSalesRecords(userId)
            dao.insertSalesRecords(remoteRecords.toEntityList())

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al sincronizar registros de ventas")
        }
    }

    override suspend fun saveSaleRecord(
        userId: String,
        amount: Double
    ): DataResult<Unit> {
        return try {
            val newRecord = SaleRecordDto(
                userId = userId,
                amount = amount
            )

            api.insertSaleRecord(newRecord)
            syncSalesRecords(userId)

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al guardar registro de venta")
        }
    }
}