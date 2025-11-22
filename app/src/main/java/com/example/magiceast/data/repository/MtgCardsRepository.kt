package com.example.magiceast.data.repository

import com.example.magiceast.data.remote.MtgApiClient
import com.example.magiceast.data.remote.dto.MtgCardDto

class MtgCardsRepository {

    suspend fun getCards(pageSize: Int = 20): Result<List<MtgCardDto>> {
        return runCatching {
            val response = MtgApiClient.api.getCards(pageSize = pageSize)
            response.cards
        }
    }
}