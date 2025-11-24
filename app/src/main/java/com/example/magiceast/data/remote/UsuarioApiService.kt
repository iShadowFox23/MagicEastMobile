package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioApiDto): Response<Usuario>

    @GET("usuarios/{email}")
    suspend fun obtenerUsuarioPorEmail(@Path("email") email: String): Usuario

}
