package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioApiDto): Response<Usuario>
}
