package com.pdm0126.proyectopdm.data.remote

import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.postgrest.result.*
import io.github.jan.supabase.postgrest.postgrest

class ProfileApi {

    suspend fun getProfiles(): List<ProfileDto> {
        return ApiClient.supabase.postgrest["profiles"]
            .select()
            .decodeList<ProfileDto>()
    }

    suspend fun insertProfile(profile: ProfileDto) {
        ApiClient.supabase.postgrest["profiles"]
            .insert(profile)
    }
}