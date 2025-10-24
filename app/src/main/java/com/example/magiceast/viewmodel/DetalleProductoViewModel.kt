package com.example.magiceast.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.magiceast.model.Producto

class DetalleProductoViewModel : ViewModel() {

    var cantidad = mutableStateOf(1)
        private set

    var mensajeSnack = mutableStateOf<String?>(null)
        private set

    fun aumentarCantidad(stock: Int) {
        if (cantidad.value < stock) {
            cantidad.value++
        }
    }

    fun disminuirCantidad() {
        if (cantidad.value > 1) {
            cantidad.value--
        }
    }

    fun setMensaje(mensaje: String) {
        mensajeSnack.value = mensaje
    }

    fun resetMensaje() {
        mensajeSnack.value = null
    }

    fun puedeAgregar(producto: Producto): Boolean {
        return producto.stock > 0
    }
}
