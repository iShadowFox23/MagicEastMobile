package com.example.magiceast.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.model.Carta
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.repository.MtgCardsRepository
import kotlinx.coroutines.launch

data class MtgCardsUiState(
    val loading: Boolean = false,
    val cards: List<Carta> = emptyList(),
    val error: String? = null,
    val pages: Int = 1,
    val currentPage: Int = 1
)

class MtgCardsViewModel(
    private val repository: MtgCardsRepository = MtgCardsRepository()
) : ViewModel() {

    var uiState by mutableStateOf(MtgCardsUiState())
        private set

    private var lastQuery = "game:paper"

    fun cargarPaginasDisponibles(query: String = "game:paper") {
        lastQuery = query

        uiState = uiState.copy(loading = true, error = null)

        viewModelScope.launch {
            repository.getPageResponse(query, 1)
                .onSuccess { response ->

                    val totalPaginas = if (response.total_cards != null) {
                        (response.total_cards + 174) / 175   // ← Scryfall: 175 cartas por página
                    } else {
                        1
                    }

                    uiState = uiState.copy(
                        loading = false,
                        pages = totalPaginas,
                        currentPage = 1,
                        cards = response.data.map { it.toDomain() }
                    )
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        loading = false,
                        error = error.message ?: "Error al cargar cartas"
                    )
                }
        }
    }

    fun cargarPagina(page: Int) {

        uiState = uiState.copy(loading = true, error = null)

        viewModelScope.launch {
            repository.getCards(lastQuery, page)
                .onSuccess { cards ->
                    uiState = uiState.copy(
                        loading = false,
                        cards = cards,
                        currentPage = page
                    )
                }
                .onFailure { error ->
                    uiState = uiState.copy(
                        loading = false,
                        error = error.message ?: "Error al cargar página $page"
                    )
                }
        }
    }
}
