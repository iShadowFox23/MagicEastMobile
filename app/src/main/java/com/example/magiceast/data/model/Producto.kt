package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.ProductoApiDto
import com.example.magiceast.model.Producto

// Convertir desde backend → app
fun ProductoApiDto.toDomain(): Producto {

    // Convertir ruta de imagen del backend → asset interno
    val assetPath = imagen?.let { raw ->
        val cleaned = if (raw.startsWith("/")) raw.removePrefix("/") else raw
        "file:///android_asset/$cleaned"
    }

    return Producto(
        id = id,
        precio = precio,
        precioAntiguo = precio,     // no existe en backend, se iguala
        descuento = 0,              // Android lo calcula si quiere
        stock = stock,
        nombre = nombre,
        categoria = categorias,
        imagen = assetPath,
        descripcion = descripcion,
        estado = "Nuevo"            // siempre generado en Android
    )
}

// Convertir desde app → backend
fun Producto.toDto(): ProductoApiDto {
    return ProductoApiDto(
        id = id,
        nombre = nombre,
        marca = "MagicEast",                  // 👈 AHORA SÍ: null válido para Oracle
        categorias = categoria,
        precio = precio,
        stock = stock,
        descripcion = descripcion ?: "",
        imagen = imagen                // si es null el backend lo ignora
    )
}
