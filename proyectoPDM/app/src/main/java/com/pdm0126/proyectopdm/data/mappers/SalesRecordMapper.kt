package com.pdm0126.proyectopdm.data.mappers

import com.pdm0126.proyectopdm.data.entity.SaleRecordEntity
import com.pdm0126.proyectopdm.data.model.SaleRecord
import com.pdm0126.proyectopdm.data.remote.SaleRecordDto

fun SaleRecordDto.toEntity(): SaleRecordEntity {
    return SaleRecordEntity(
        id = id,
        userId = userId,
        amount = amount,
        createdAt = createdAt
    )
}

fun List<SaleRecordDto>.toEntityList(): List<SaleRecordEntity> {
    return map { it.toEntity() }
}

fun SaleRecordEntity.toDomain(): SaleRecord {
    return SaleRecord(
        id = id,
        userId = userId,
        amount = amount,
        createdAt = createdAt
    )
}

fun List<SaleRecordEntity>.toDomainList(): List<SaleRecord> {
    return map { it.toDomain() }
}