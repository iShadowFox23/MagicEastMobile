package com.example.magiceast.data.repository

import com.example.magiceast.data.remote.MtgApiClient
import com.example.magiceast.data.remote.dto.ScryfallCardDto

class MtgCardsRepository {

    suspend fun getCards(query: String = "game:paper"): Result<List<ScryfallCardDto>> {
        return runCatching {
            val response = MtgApiClient.api.searchCards(query = query)
            response.data
        }
    }
}