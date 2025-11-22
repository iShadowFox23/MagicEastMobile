package com.example.magiceast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScryfallCardsResponse(

    // Total de cartas encontradas por la búsqueda (necesario para calcular las páginas)
    @SerializedName("total_cards")
    val total_cards: Int? = null,

    // Lista de cartas de la página actual
    val data: List<ScryfallCardDto> = emptyList(),

    // Indica si existe otra página
    @SerializedName("has_more")
    val hasMore: Boolean = false,

    // URL hacia la siguiente página (Scryfall la envía)
    @SerializedName("next_page")
    val nextPage: String? = null
)
