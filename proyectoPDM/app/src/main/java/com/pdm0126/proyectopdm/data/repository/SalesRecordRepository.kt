package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.SaleRecord
import kotlinx.coroutines.flow.Flow

interface SalesRecordRepository {

    fun getSalesRecords(userId: String): Flow<List<SaleRecord>>

    suspend fun syncSalesRecords(userId: String)

    suspend fun saveSaleRecord(
        userId: String,
        amount: Double
    )
}