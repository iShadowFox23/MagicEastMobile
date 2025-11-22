package com.example.magiceast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScryfallCardsResponse(
    val data: List<ScryfallCardDto> = emptyList(),

    @SerializedName("has_more")
    val hasMore: Boolean = false,

    @SerializedName("next_page")
    val nextPage: String? = null
)
