package com.example.magiceast.model

data class Producto(
    val id: Int,
    val precio: Int,
    val precioAntiguo: Int,
    val descuento: Int,
    val stock: Int,
    val nombre: String,
    val categoria: String,
    val imagen: String?,
    val descripcion: String?,
    val estado: String
)