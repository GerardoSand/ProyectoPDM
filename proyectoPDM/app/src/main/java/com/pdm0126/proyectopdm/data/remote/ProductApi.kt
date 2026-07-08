package com.pdm0126.proyectopdm.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApi {

    suspend fun getProducts(): List<ProductDto> {
        return ApiClient.client.get(
            "${ApiClient.SUPABASE_URL}/rest/v1/products?select=*"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
        }.body()
    }

    suspend fun createProduct(product: ProductDto): List<ProductDto> {
        return ApiClient.client.post(
            "${ApiClient.SUPABASE_URL}/rest/v1/products"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
    }

    suspend fun updateProduct(
        productId: String,
        product: ProductDto
    ): List<ProductDto> {
        return ApiClient.client.patch(
            "${ApiClient.SUPABASE_URL}/rest/v1/products?id=eq.$productId"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(product)
        }.body()
    }

    suspend fun disableProduct(productId: String): List<ProductDto> {
        return ApiClient.client.patch(
            "${ApiClient.SUPABASE_URL}/rest/v1/products?id=eq.$productId"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "is_active" to false
                )
            )
        }.body()
    }
}