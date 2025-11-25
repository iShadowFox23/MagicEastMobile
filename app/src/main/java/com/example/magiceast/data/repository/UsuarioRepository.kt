package com.example.magiceast.data.repository

import com.example.magiceast.data.remote.UsuarioApiClient
import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario
import retrofit2.Response

class UsuarioRepository {

    // REGISTRAR
    suspend fun registrarUsuario(usuario: UsuarioApiDto): Usuario? {
        val response = UsuarioApiClient.api.registrarUsuario(usuario)
        return if (response.isSuccessful) response.body() else null
    }

    // OBTENER POR EMAIL
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        return try {
            UsuarioApiClient.api.obtenerUsuarioPorEmail(email)
        } catch (e: Exception) {
            null
        }
    }

    // ➤ LISTAR
    suspend fun listarUsuarios(): List<Usuario>? {
        return try {
            UsuarioApiClient.api.listarUsuarios()
        } catch (e: Exception) {
            null
        }
    }

    // ➤ ELIMINAR
    suspend fun eliminarUsuario(id: Long): Boolean {
        return try {
            val response: Response<Unit> = UsuarioApiClient.api.eliminarUsuario(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
