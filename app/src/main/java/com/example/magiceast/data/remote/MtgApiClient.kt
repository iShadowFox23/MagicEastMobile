package com.example.magiceast.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MtgApiClient {

    private const val BASE_URL = "https://api.magicthegathering.io/v1/"

    val api: MtgApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MtgApiService::class.java)
}