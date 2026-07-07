package com.pdm0126.proyectopdm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey
    val id: String,
    val fullName: String?,
    val role: String
)