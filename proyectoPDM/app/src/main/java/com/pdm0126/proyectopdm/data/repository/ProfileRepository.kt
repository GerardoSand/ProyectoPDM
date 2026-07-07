package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomain
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.remote.ProfileApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(context: Context) {

    private val api = ProfileApi()
    private val dao = DatabaseProvider
        .getDatabase(context)
        .profileDao()

    fun getProfiles(): Flow<List<Profile>> {
        return dao.getProfiles()
            .map { it.toDomainList() }
    }

    suspend fun getProfileById(id: String): Profile? {
        return dao.getProfileById(id)?.toDomain()
    }

    suspend fun syncProfiles() {
        val remoteProfiles = api.getProfiles()
        dao.clearProfiles()
        dao.insertProfiles(remoteProfiles.toEntityList())
    }
}