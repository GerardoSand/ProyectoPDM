package com.pdm0126.proyectopdm.data.mappers

import com.pdm0126.proyectopdm.data.entity.ProductEntity
import com.pdm0126.proyectopdm.data.remote.ProductDto
import com.pdm0126.proyectopdm.data.model.Product

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id ?: "",
        name = name,
        brand = brand,
        priceContado = priceContado,
        priceCuotas = priceCuotas,
        category = category,
        imageUrl = imageUrl,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun List<ProductDto>.toEntityList(): List<ProductEntity> {
    return map { it.toEntity() }
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        priceContado = priceContado,
        priceCuotas = priceCuotas,
        category = category,
        imageUrl = imageUrl,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun List<ProductEntity>.toDomainList(): List<Product> {
    return map { it.toDomain() }
}

fun Product.toDto(): ProductDto {
    return ProductDto(
        id = id,
        name = name,
        brand = brand,
        priceContado = priceContado,
        priceCuotas = priceCuotas,
        category = category,
        isActive = isActive,
        imageUrl = imageUrl,
        createdAt = createdAt
    )
}