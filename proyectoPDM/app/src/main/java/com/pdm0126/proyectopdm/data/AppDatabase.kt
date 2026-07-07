package com.pdm0126.proyectopdm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pdm0126.proyectopdm.data.dao.ProductDao
import com.pdm0126.proyectopdm.data.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}