package com.pdm0126.proyectopdm.data.remote

import io.github.jan.supabase.postgrest.query.*
import io.github.jan.supabase.postgrest.result.*
import io.github.jan.supabase.postgrest.postgrest

class SalesRecordApi {

    suspend fun getSalesRecords(userId: String): List<SaleRecordDto> {
        return ApiClient.supabase.postgrest["sales_records"]
            .select {
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList<SaleRecordDto>()
    }

    suspend fun getAllSalesRecords(): List<SaleRecordDto> {
        return ApiClient.supabase.postgrest["sales_records"]
            .select()
            .decodeList<SaleRecordDto>()
    }

    suspend fun insertSaleRecord(saleRecord: SaleRecordDto) {
        ApiClient.supabase.postgrest["sales_records"]
            .insert(saleRecord)
    }
}