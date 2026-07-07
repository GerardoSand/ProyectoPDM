package com.pdm0126.proyectopdm.data.remote

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SalesRecordApi {

    suspend fun getSalesRecords(userId: String): List<SaleRecordDto> {
        return ApiClient.client.get(
            "${ApiClient.SUPABASE_URL}/rest/v1/sales_records?user_id=eq.$userId&select=*"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
        }.body()
    }

    suspend fun insertSaleRecord(saleRecord: SaleRecordDto) {
        ApiClient.client.post(
            "${ApiClient.SUPABASE_URL}/rest/v1/sales_records"
        ) {
            header("apikey", ApiClient.SUPABASE_KEY)
            header("Authorization", "Bearer ${ApiClient.SUPABASE_KEY}")
            header("Prefer", "return=minimal")
            contentType(ContentType.Application.Json)
            setBody(saleRecord)
        }
    }
}