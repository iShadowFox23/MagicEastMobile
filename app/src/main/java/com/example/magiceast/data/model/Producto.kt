package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.ProductoApiDto
import com.example.magiceast.model.Producto

fun ProductoApiDto.toDomain(): Producto {
    return Producto(
    id = id,
    precio = precio,
    precioAntiguo = precio,
    descuento = 0,
    stock = stock,
    nombre = nombre,
    categoria = categorias,
    imagen = null,
    descripcion = descripcion,
    estado = "Nuevo"
    )
}