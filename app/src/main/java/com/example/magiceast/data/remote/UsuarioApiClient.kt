package com.example.magiceast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UsuarioApiClient {

    private const val BASE_URL = "http://3.135.235.62:8080/api/"



    val api: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }
}
