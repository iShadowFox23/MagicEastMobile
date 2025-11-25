package com.example.magiceast.data.repository

import com.example.magiceast.data.model.Carta
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.remote.MtgApiClient

class MtgCardsRepository {

    suspend fun getPageResponse(query: String, page: Int) = runCatching {
        MtgApiClient.api.getCards(
            query = query,
            page = page
        )
    }


    suspend fun getCards(query: String, page: Int) = runCatching {
        val response = MtgApiClient.api.getCards(
            query = query,
            page = page
        )

        response.data.map { it.toDomain() }
    }


    suspend fun getCardById(id: String) = runCatching {
        MtgApiClient.api.getCardById(id).toDomain()
    }
}
