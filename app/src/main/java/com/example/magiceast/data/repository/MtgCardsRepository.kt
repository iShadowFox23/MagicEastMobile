package com.example.magiceast.data.repository

import com.example.magiceast.data.model.Carta
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.remote.MtgApiClient


class MtgCardsRepository {

    suspend fun getCards(query: String = "game:paper"): Result<List<Carta>> {
        return runCatching {
            val response = MtgApiClient.api.searchCards(query = query)
            response.data
                .map { it.toDomain() }
        }
    }
    //Obetener por ID
    suspend fun getCardById(id: String): Result<Carta> {
        return runCatching {
            val dto = MtgApiClient.api.getCardById(id)
            dto.toDomain()
        }
    }
}