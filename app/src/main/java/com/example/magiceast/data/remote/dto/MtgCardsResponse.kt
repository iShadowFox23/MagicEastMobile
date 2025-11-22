package com.example.magiceast.data.remote.dto

data class MtgCardsResponse(
    val cards: List<MtgCardDto> = emptyList()
)