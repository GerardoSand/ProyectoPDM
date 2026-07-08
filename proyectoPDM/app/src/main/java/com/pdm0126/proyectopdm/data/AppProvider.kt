package com.pdm0126.proyectopdm.data

import android.content.Context
import com.pdm0126.proyectopdm.data.repository.ProductRepository
import com.pdm0126.proyectopdm.data.repository.ProductRepositoryImpl
import com.pdm0126.proyectopdm.data.repository.ProfileRepository
import com.pdm0126.proyectopdm.data.repository.ProfileRepositoryImpl
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepository
import com.pdm0126.proyectopdm.data.repository.SalesRecordRepositoryImpl

object AppProvider {

    fun productRepository(context: Context): ProductRepository {
        return ProductRepositoryImpl(context)
    }

    fun profileRepository(context: Context): ProfileRepository {
        return ProfileRepositoryImpl(context)
    }

    fun salesRecordRepository(context: Context): SalesRecordRepository {
        return SalesRecordRepositoryImpl(context)
    }
}