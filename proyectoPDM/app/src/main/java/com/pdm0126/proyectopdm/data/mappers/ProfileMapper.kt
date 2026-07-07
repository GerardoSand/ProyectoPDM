package com.pdm0126.proyectopdm.data.mappers

import com.pdm0126.proyectopdm.data.entity.ProfileEntity
import com.pdm0126.proyectopdm.data.model.Profile
import com.pdm0126.proyectopdm.data.remote.ProfileDto

fun ProfileDto.toEntity(): ProfileEntity {
    return ProfileEntity(
        id = id,
        fullName = fullName,
        role = role
    )
}

fun List<ProfileDto>.toEntityList(): List<ProfileEntity> {
    return map { it.toEntity() }
}

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = id,
        fullName = fullName,
        role = role
    )
}

fun List<ProfileEntity>.toDomainList(): List<Profile> {
    return map { it.toDomain() }
}