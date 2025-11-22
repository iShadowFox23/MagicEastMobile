package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.ScryfallCardsResponse
import com.example.magiceast.data.remote.dto.ScryfallCardDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface MtgApiService {
    //Listado de cartas
    @GET("cards/search")
    suspend fun getCards(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): ScryfallCardsResponse
    //Carta Single
    @GET("cards/{id}")
    suspend fun getCardById(
        @Path("id") id: String
    ): ScryfallCardDto
}