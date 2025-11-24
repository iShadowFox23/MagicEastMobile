package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.ProductoApiDto
import com.example.magiceast.model.Producto

fun ProductoApiDto.toDomain(): Producto {

    // imagen: "/images/precons/precon11.jpg"
    // queremos: "file:///android_asset/images/precons/precon11.jpg"
    val assetPath = imagen?.let { raw ->
        // Por si viene con "/" al inicio:
        val cleaned = if (raw.startsWith("/")) raw.removePrefix("/") else raw
        "file:///android_asset/$cleaned"
    }

    return Producto(
        id = id,
        precio = precio,
        precioAntiguo = precio,
        descuento = 0,
        stock = stock,
        nombre = nombre,
        categoria = categorias,
        imagen = assetPath,
        descripcion = descripcion,
        estado = "Nuevo"
    )
}