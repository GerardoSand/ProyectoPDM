package com.pdm0126.proyectopdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val brand: String,
    val priceContado: Double,
    val priceCuotas: Double,
    val category: String,
    val imageUrl: String?,
    val isActive: Boolean,
    val createdAt: String?
)