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

// 🔹 Conversión del DTO a tu modelo interno de forma segura
fun ScryfallCardDto.toDomain(): Carta {

    // Si la carta es de doble cara, tomamos la cara frontal
    val face = cardFaces?.firstOrNull()

    val finalName = name ?: face?.name
    val finalManaCost = manaCost ?: face?.manaCost
    val finalTypeLine = typeLine ?: face?.typeLine

    // Imagen final:
    // 1) Si existe imageUris.normal → úsalo
    // 2) si no → toma la imagen de la cara frontal
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
        valor = Random.nextInt(from = 500, until = 100_001), // 500 a 100.000 CLP
        stock = Random.nextInt(0, 11)
    )
}
