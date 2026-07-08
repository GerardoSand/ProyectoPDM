package com.pdm0126.proyectopdm.data.repository

import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfiles(): Flow<List<Profile>>
    suspend fun getProfileById(id: String): Profile?
<<<<<<< HEAD
    suspend fun syncProfiles(): DataResult<Unit>
=======
    suspend fun syncProfiles()
    suspend fun insertProfile(profile: Profile)
>>>>>>> 8b91bae (ui)
}