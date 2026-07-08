package com.pdm0126.proyectopdm.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String? = null,

    val name: String,
    val brand: String,

    @SerialName("price_contado")
    val priceContado: Double,

    @SerialName("price_cuotas")
    val priceCuotas: Double,

    val category: String,

    @SerialName("is_active")
    val isActive: Boolean = true,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)