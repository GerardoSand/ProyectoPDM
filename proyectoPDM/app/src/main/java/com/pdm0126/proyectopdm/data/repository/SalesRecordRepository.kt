package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow

interface SalesRecordRepository {
    fun getSalesRecords(userId: String): Flow<List<SaleRecord>>
<<<<<<< HEAD
    suspend fun syncSalesRecords(userId: String): DataResult<Unit>
    suspend fun saveSaleRecord(userId: String, amount: Double): DataResult<Unit>
=======

    suspend fun syncSalesRecords(userId: String)

    suspend fun saveSaleRecord(
        userId: String,
        amount: Double
    )

    suspend fun getAllSalesRecords(): List<SaleRecord>
>>>>>>> 8b91bae (ui)
}