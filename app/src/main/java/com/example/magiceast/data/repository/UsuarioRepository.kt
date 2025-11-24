package com.example.magiceast.data.repository

import com.example.magiceast.data.remote.ApiClient
import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario

class UsuarioRepository {

    suspend fun registrarUsuario(usuario: UsuarioApiDto): Usuario? {
        val response = ApiClient.api.registrarUsuario(usuario)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
