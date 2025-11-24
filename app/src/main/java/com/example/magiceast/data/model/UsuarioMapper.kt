package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario

fun UsuarioApiDto.toDomain(): Usuario {
    return Usuario(
        id = 0,
        nombre = nombre,
        email = email,
        direccion = direccion,
        contrasena = contrasena
    )
}
