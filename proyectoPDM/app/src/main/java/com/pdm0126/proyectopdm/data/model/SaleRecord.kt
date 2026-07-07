package com.pdm0126.proyectopdm.data.model

data class SaleRecord(
    val id: String,
    val userId: String,
    val amount: Double,
    val createdAt: String?
)