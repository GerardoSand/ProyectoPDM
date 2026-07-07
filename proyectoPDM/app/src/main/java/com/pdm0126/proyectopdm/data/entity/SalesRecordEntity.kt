package com.pdm0126.proyectopdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales_records")
data class SaleRecordEntity(

    @PrimaryKey val id: String,

    val userId: String,

    val amount: Double,

    val createdAt: String

)