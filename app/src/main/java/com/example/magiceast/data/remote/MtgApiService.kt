package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.ScryfallCardsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MtgApiService {

    @GET("cards/search")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("page") page: Int? = null
    ): ScryfallCardsResponse
}