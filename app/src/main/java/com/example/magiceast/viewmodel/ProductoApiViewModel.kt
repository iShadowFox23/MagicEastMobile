package com.example.magiceast.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.repository.ProductoApiRepository
import com.example.magiceast.model.Producto
import kotlinx.coroutines.launch

data class ProductoApiUiState(
    val loading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null
)

class ProductoApiViewModel(
    private val repository: ProductoApiRepository = ProductoApiRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ProductoApiUiState())
        private set

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        uiState = uiState.copy(loading = true, error = null)

        viewModelScope.launch {
            try {
                val lista = repository.listarProductos()
                uiState = uiState.copy(
                    loading = false,
                    productos = lista,
                    error = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = "Error al cargar productos"
                )
            }
        }
    }
}