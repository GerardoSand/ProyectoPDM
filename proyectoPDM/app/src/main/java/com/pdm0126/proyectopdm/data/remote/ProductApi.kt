package com.pdm0126.proyectopdm.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ProductApi {

    suspend fun getProducts(): List<ProductDto> {
        return ApiClient.client.get(
            "${ApiClient.SUPABASE_URL}/rest/v1/products?select=*"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
        }.body()
    }
}