package com.pdm0126.proyectopdm.data.repository

import android.content.Context
import com.pdm0126.proyectopdm.data.DatabaseProvider
import com.pdm0126.proyectopdm.data.mappers.toDomain
import com.pdm0126.proyectopdm.data.mappers.toDomainList
import com.pdm0126.proyectopdm.data.mappers.toEntityList
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.remote.ProfileApi
import com.pdm0126.proyectopdm.data.util.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(context: Context) : ProfileRepository {

    private val api = ProfileApi()
    private val dao = DatabaseProvider
        .getDatabase(context)
        .profileDao()

    override fun getProfiles(): Flow<List<Profile>> {
        return dao.getProfiles()
            .map { it.toDomainList() }
    }

    override suspend fun getProfileById(id: String): Profile? {
        return dao.getProfileById(id)?.toDomain()
    }

    override suspend fun syncProfiles(): DataResult<Unit> {
        return try {
            val remoteProfiles = api.getProfiles()
            dao.clearProfiles()
            dao.insertProfiles(remoteProfiles.toEntityList())

            DataResult.Success(Unit)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Error al sincronizar perfiles")
        }
    }

    override suspend fun insertProfile(profile: Profile) {
        val dto = com.pdm0126.proyectopdm.data.remote.ProfileDto(
            id = profile.id,
            fullName = profile.fullName,
            role = profile.role
        )
        api.insertProfile(dto)
        val entity = com.pdm0126.proyectopdm.data.entity.ProfileEntity(
            id = profile.id,
            fullName = profile.fullName,
            role = profile.role
        )
        dao.insertProfiles(listOf(entity))
    }
}