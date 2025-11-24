package com.example.magiceast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.repository.ProductoApiRepository
import com.example.magiceast.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: ProductoApiRepository = ProductoApiRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> get() = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error


    // 🔹 Cargar productos desde backend
    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val lista = repository.listarProductos()
                _productos.value = lista
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error cargando productos"
            }
        }
    }

    // 🔹 Agregar producto (POST al backend)
    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            val creado = repository.crearProducto(producto)
            if (creado != null) {
                _productos.value = _productos.value + creado
                _error.value = null
            } else {
                _error.value = "No se pudo crear el producto"
            }
        }
    }

    // 🔹 Editar producto (PUT al backend)
    fun editarProducto(producto: Producto) {
        viewModelScope.launch {
            val actualizado = repository.actualizarProducto(producto)
            if (actualizado != null) {
                _productos.value = _productos.value.map {
                    if (it.id == actualizado.id) actualizado else it
                }
                _error.value = null
            } else {
                _error.value = "No se pudo actualizar el producto"
            }
        }
    }

    // 🔹 Eliminar producto (DELETE al backend)
    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            val eliminado = repository.eliminarProducto(id)
            if (eliminado) {
                _productos.value = _productos.value.filter { it.id != id }
                _error.value = null
            } else {
                _error.value = "No se pudo eliminar el producto"
            }
        }
    }
}
