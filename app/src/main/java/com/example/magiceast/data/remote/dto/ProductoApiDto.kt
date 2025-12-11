package com.example.magiceast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductoApiDto(
    val id: Int,
    val nombre: String,
    val marca: String,
    val categorias: String,
    val precio: Int,
    val stock: Int,
    val descripcion: String,
    val imagen: String?,
    @SerializedName("set_name")
    val setName: String? = null
)