package com.pdm0126.proyectopdm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pdm0126.proyectopdm.data.dao.ProductDao
import com.pdm0126.proyectopdm.data.dao.SalesRecordDao
import com.pdm0126.proyectopdm.data.entity.ProductEntity
import com.pdm0126.proyectopdm.data.entity.SaleRecordEntity

@Database(
    entities = [ProductEntity::class, SaleRecordEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun salesRecordDao(): SalesRecordDao
}