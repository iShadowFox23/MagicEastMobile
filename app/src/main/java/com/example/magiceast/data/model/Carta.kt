package com.example.magiceast.data.model

import com.example.magiceast.data.remote.dto.ScryfallCardDto
import kotlin.random.Random

data class Carta(
    val id: String?,
    val name: String?,
    val manaCost: String?,
    val typeLine: String?,
    val imageUrl: String?,
    val valor: Int,
    val stock: Int
)


fun ScryfallCardDto.toDomain(): Carta {


    val face = cardFaces?.firstOrNull()

    val finalName = name ?: face?.name
    val finalManaCost = manaCost ?: face?.manaCost
    val finalTypeLine = typeLine ?: face?.typeLine

    val finalImage =
        imageUris?.normal
            ?: face?.imageUris?.normal
            ?: face?.imageUris?.large
            ?: imageUris?.large

    return Carta(
        id = id,
        name = finalName ?: "Unknown",
        manaCost = finalManaCost,
        typeLine = finalTypeLine,
        imageUrl = finalImage,
        valor = Random.nextInt(from = 500, until = 100_001),
        stock = Random.nextInt(0, 11)
    )
}
