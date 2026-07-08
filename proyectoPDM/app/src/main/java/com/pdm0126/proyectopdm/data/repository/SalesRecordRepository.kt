package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow

interface SalesRecordRepository {
    fun getSalesRecords(userId: String): Flow<List<SaleRecord>>
    suspend fun syncSalesRecords(userId: String): DataResult<Unit>
    suspend fun saveSaleRecord(userId: String, amount: Double): DataResult<Unit>
}