package com.pdm0126.proyectopdm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pdm0126.proyectopdm.data.entity.SaleRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesRecordDao {
    @Query("SELECT * FROM sales_records WHERE userId = :userId ORDER BY createdAt DESC")
    fun getSalesRecords(userId: String): Flow<List<SaleRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalesRecords(records: List<SaleRecordEntity>)

    @Query("DELETE FROM sales_records WHERE userId = :userId")
    suspend fun clearSalesRecords(userId: String)
}