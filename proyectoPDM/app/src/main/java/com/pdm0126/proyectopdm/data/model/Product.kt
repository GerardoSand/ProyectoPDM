package com.pdm0126.proyectopdm.data.model

data class Product(
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