package com.example.magiceast.data.remote.dto

data class ProductoApiDto(
    val id: Int,
    val nombre: String,
    val marca: String,
    val categorias: String,
    val precio: Int,
    val stock: Int,
    val descripcion: String

)