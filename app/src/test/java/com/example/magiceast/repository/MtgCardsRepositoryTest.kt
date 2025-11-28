package com.example.magiceast.data.repository

import com.example.magiceast.data.model.Carta
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.remote.MtgApiClient
import com.example.magiceast.data.remote.dto.ImageUrisDto
import com.example.magiceast.data.remote.dto.ScryfallCardDto
import com.example.magiceast.data.remote.dto.ScryfallCardsResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MtgCardsRepositoryTest {

    private lateinit var repository: MtgCardsRepository

    @Before
    fun configurar() {
        repository = MtgCardsRepository()
        mockkObject(MtgApiClient)
        MtgApiClient.api = mockk()
    }

    @Test
    fun `obtenerPagina devuelve resultado exitoso`() = runTest {
        val dto = ScryfallCardDto(
            id = "123",
            name = "Lightning Bolt",
            manaCost = "{R}",
            typeLine = "Instant",
            imageUris = ImageUrisDto(normal = "http://img.com/card.jpg"),
            cardFaces = null
        )
        val respuesta = ScryfallCardsResponse(data = listOf(dto))

        coEvery { MtgApiClient.api.getCards("bolt", 1) } returns respuesta

        val resultado = repository.getPageResponse("bolt", 1)

        assertTrue(resultado.isSuccess)
        assertEquals(respuesta, resultado.getOrNull())
    }

    @Test
    fun `obtenerCartas mapea correctamente a modelo de dominio`() = runTest {
        val dto = ScryfallCardDto(
            id = "abc",
            name = "Shock",
            manaCost = "{R}",
            typeLine = "Instant",
            imageUris = ImageUrisDto(normal = "http://img.com/shock.jpg"),
            cardFaces = null
        )

        val respuesta = ScryfallCardsResponse(data = listOf(dto))

        coEvery { MtgApiClient.api.getCards("shock", 2) } returns respuesta

        val resultado = repository.getCards("shock", 2)

        assertTrue(resultado.isSuccess)
        val cartas: List<Carta> = resultado.getOrNull() ?: emptyList()
        assertEquals(1, cartas.size)
        assertEquals("Shock", cartas[0].name)
        assertEquals("Instant", cartas[0].typeLine)
    }

    @Test
    fun `obtenerCartaPorId retorna carta mapeada`() = runTest {
        val dto = ScryfallCardDto(
            id = "999",
            name = "Counterspell",
            manaCost = "{U}{U}",
            typeLine = "Instant",
            imageUris = ImageUrisDto(normal = "http://img.com/counter.jpg"),
            cardFaces = null
        )

        coEvery { MtgApiClient.api.getCardById("999") } returns dto

        val resultado = repository.getCardById("999")

        assertTrue(resultado.isSuccess)
        val carta = resultado.getOrNull()
        assertEquals("Counterspell", carta?.name)
        assertEquals("Instant", carta?.typeLine)
    }
}
