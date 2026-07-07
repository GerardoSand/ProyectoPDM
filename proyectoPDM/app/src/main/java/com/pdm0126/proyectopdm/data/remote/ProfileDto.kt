package com.pdm0126.proyectopdm.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,

    @SerialName("full_name")
    val fullName: String? = null,

    val role: String
)