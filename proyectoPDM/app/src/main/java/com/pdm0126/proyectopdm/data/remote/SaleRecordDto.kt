package com.pdm0126.proyectopdm.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaleRecordDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String,
    val amount: Double,
    @SerialName("created_at") val createdAt: String? = null
)