package com.pdm0126.proyectopdm.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    const val SUPABASE_URL = "https://wneetknesbclhotyznvj.supabase.co"
    const val SUPABASE_KEY = "sb_publishable_oofsq7U2yzkRq7GtEYOy3A_FAy3HMhe"

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}