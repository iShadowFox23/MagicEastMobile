package com.example.magiceast.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.model.Carta
import com.example.magiceast.data.repository.MtgCardsRepository
import kotlinx.coroutines.launch

data class SingleCartaUiState(
    val loading: Boolean = false,
    val carta: Carta? = null,
    val error: String? = null,
    val productoSugeridoId: Int? = null
)

class SingleCartaViewModel(
    private val repository: MtgCardsRepository = MtgCardsRepository(),
    private val productoRepository: com.example.magiceast.data.repository.ProductoApiRepository = com.example.magiceast.data.repository.ProductoApiRepository()
) : ViewModel() {

    var uiState by mutableStateOf(SingleCartaUiState())
        private set

    fun loadCard(id: String) {
        uiState = uiState.copy(loading = true, error = null)

        viewModelScope.launch {
            repository.getCardById(id)
                .onSuccess { card ->
                    uiState = uiState.copy(
                        loading = false,
                        carta = card,
                        error = null
                    )
                    buscarProductoDelSet(card.setName)
                }
                .onFailure { throwable ->
                    uiState = uiState.copy(
                        loading = false,
                        error = throwable.message ?: "Error al cargar la carta"
                    )
                }
        }
    }

    private fun buscarProductoDelSet(setName: String?) {
        if (setName.isNullOrBlank()) return

        viewModelScope.launch {
            try {
                val productos = productoRepository.listarProductos()
                val productoEncontrado = productos.find { it.setName.equals(setName, ignoreCase = true) }
                
                if (productoEncontrado != null) {
                   uiState = uiState.copy(productoSugeridoId = productoEncontrado.id)
                }
            } catch (e: Exception) {
                // Si falla, simplemente no mostramos sugerencia
                e.printStackTrace()
            }
        }
    }
}
