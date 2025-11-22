package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.MtgCardsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MtgApiService {

    @GET("cards")
    suspend fun getCards(
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = 20,
        @Query("name") name: String? = null,
        @Query("types") types: String? = null
    ): MtgCardsResponse
}