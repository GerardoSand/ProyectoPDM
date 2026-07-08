package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfiles(): Flow<List<Profile>>
    suspend fun getProfileById(id: String): Profile?
    suspend fun syncProfiles()
}