package com.example.magiceast.viewmodel

import android.content.Context
import android.net.Uri
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


    fun cargarProductos() {
        viewModelScope.launch {
            try {
                _productos.value = repository.listarProductos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error cargando productos"
            }
        }
    }


    fun agregarProducto(context: Context, producto: Producto, uri: Uri?) {
        viewModelScope.launch {
            try {
                val creado = repository.crearProductoConImagen(context, producto, uri)

                if (creado != null) {
                    _productos.value = _productos.value + creado
                } else {
                    _error.value = "No se pudo crear el producto"
                }
            } catch (e: Exception) {
                _error.value = "Error al subir la imagen"
            }
        }
    }

    fun editarProducto(producto: Producto) {
        viewModelScope.launch {
            val actualizado = repository.actualizarProducto(producto)
            if (actualizado != null) {
                _productos.value = _productos.value.map {
                    if (it.id == actualizado.id) actualizado else it
                }
            } else {
                _error.value = "No se pudo actualizar"
            }
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            val eliminado = repository.eliminarProducto(id)
            if (eliminado) {
                _productos.value = _productos.value.filter { it.id != id }
            } else {
                _error.value = "No se pudo eliminar"
            }
        }
    }
}
