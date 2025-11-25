package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioApiDto): Response<Usuario>

    @GET("usuarios/{email}")
    suspend fun obtenerUsuarioPorEmail(@Path("email") email: String): Usuario

    @GET("usuarios")
    suspend fun listarUsuarios(): List<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long): Response<Unit>
}
