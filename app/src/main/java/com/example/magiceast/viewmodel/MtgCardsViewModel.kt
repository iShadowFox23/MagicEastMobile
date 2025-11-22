package com.example.magiceast.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.remote.dto.MtgCardDto
import com.example.magiceast.data.repository.MtgCardsRepository
import kotlinx.coroutines.launch

data class MtgCardsUiState(
    val loading: Boolean = false,
    val cards: List<MtgCardDto> = emptyList(),
    val error: String? = null
)

class MtgCardsViewModel(
    private val repository: MtgCardsRepository = MtgCardsRepository()
) : ViewModel() {

    var uiState by mutableStateOf(MtgCardsUiState())
        private set

    fun cargarCards(pageSize: Int = 20) {
        uiState = uiState.copy(loading = true, error = null)

        viewModelScope.launch {
            repository.getCards(pageSize)
                .onSuccess { cards ->
                    uiState = uiState.copy(
                        loading = false,
                        cards = cards,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        loading = false,
                        error = throwable.message ?: "Error al cargar cartas"
                    )
                }
        }
    }
}