package com.example.magiceast.model

data class Usuario(
    val id: Long = 0,
    val nombre: String,
    val email: String,
    val direccion: String,
    val contrasena: String
)
