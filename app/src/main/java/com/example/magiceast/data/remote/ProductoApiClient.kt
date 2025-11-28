package com.example.magiceast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductoApiClient {

    private const val BASE_URL = "http://3.135.235.62:8080/api/"

    val api: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }
}
