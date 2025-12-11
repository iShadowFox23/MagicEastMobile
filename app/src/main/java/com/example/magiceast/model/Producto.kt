package com.example.magiceast.model

import com.example.magiceast.util.Constants

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
    val estado: String,
    val setName: String? = null
) {

    val imagenUrl: String?
        get() = imagen?.let { name ->
            if (name.startsWith("http")) name
            else Constants.IMAGENES_URL + name
        }
}
