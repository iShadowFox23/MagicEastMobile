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
    return Carta(
        id = id,
        name = name,
        manaCost = manaCost,
        typeLine = typeLine,
        imageUrl = imageUris?.normal,
        valor = Random.nextInt(from = 500, until = 100_001), // 500 a 100.000 CLP
        stock = Random.nextInt(0, 11)
    )
}
