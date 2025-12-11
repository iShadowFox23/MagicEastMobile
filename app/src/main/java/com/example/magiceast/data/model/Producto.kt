package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.ProductoApiDto
import com.example.magiceast.model.Producto


private const val BASE_IMAGE_URL = "http://3.135.235.62:8080/api/productos/imagenes/"



fun ProductoApiDto.toDomain(): Producto {


    var imagenCompleta = imagen?.let { fileName ->
        if (fileName.startsWith("http")) fileName
        else BASE_IMAGE_URL + fileName
    }

    return Producto(
        id = id,
        precio = precio,
        precioAntiguo = precio,
        descuento = 0,
        stock = stock,
        nombre = nombre,
        categoria = categorias,
        imagen = imagenCompleta,
        descripcion = descripcion,
        estado = "Nuevo",
        setName = setName
    )
}


fun Producto.toDto(): ProductoApiDto {
    return ProductoApiDto(
        id = id,
        nombre = nombre,
        marca = "MagicEast",
        categorias = categoria,
        precio = precio,
        stock = stock,
        descripcion = descripcion ?: "",
        imagen = imagen?.substringAfterLast("/"),
        setName = setName
    )
}
