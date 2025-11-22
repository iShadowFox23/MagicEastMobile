package com.example.magiceast.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScryfallCardDto(

    val id: String? = null,
    val name: String? = null,

    @SerializedName("mana_cost")
    val manaCost: String? = null,

    @SerializedName("type_line")
    val typeLine: String? = null,

    // Imagen para cartas normales
    @SerializedName("image_uris")
    val imageUris: ImageUrisDto? = null,


    @SerializedName("card_faces")
    val cardFaces: List<ScryfallCardFaceDto>? = null
)


data class ScryfallCardFaceDto(

    val name: String? = null,

    @SerializedName("mana_cost")
    val manaCost: String? = null,

    @SerializedName("type_line")
    val typeLine: String? = null,

    @SerializedName("image_uris")
    val imageUris: ImageUrisDto? = null
)

data class ImageUrisDto(
    val small: String? = null,
    val normal: String? = null,
    val large: String? = null,
    val png: String? = null,

    @SerializedName("art_crop")
    val artCrop: String? = null,

    @SerializedName("border_crop")
    val borderCrop: String? = null
)
