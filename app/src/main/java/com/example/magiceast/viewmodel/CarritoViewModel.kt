package com.example.magiceast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.magiceast.model.Producto
import com.example.magiceast.data.model.Carta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ItemCarrito(val producto: Producto, val cantidad: Int = 1)

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito = _carrito.asStateFlow()

    fun agregarProducto(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val item = listaActual[index]
            if (item.cantidad < producto.stock) {
                listaActual[index] = item.copy(cantidad = item.cantidad + 1)
            }
        } else {
            listaActual.add(ItemCarrito(producto))
        }

        _carrito.value = listaActual.toList()
    }

    fun agregarCartaAlCarrito(carta: Carta, cantidad: Int) {

        // Conversion de carta a un Producto
        val producto = Producto(
            id = carta.id?.hashCode() ?: carta.hashCode(),
            precio = carta.valor,
            precioAntiguo = carta.valor,
            descuento = 0,
            stock = carta.stock,
            nombre = carta.name ?: "Carta sin nombre",
            categoria = "Cartas MTG",
            imagen = carta.imageUrl,
            descripcion = carta.typeLine,
            estado = "Nuevo"
        )

        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val item = listaActual[index]
            val nuevaCantidad = (item.cantidad + cantidad).coerceAtMost(producto.stock)
            listaActual[index] = item.copy(cantidad = nuevaCantidad)
        } else {
            val cantidadFinal = cantidad.coerceAtMost(producto.stock)
            if (cantidadFinal > 0) {
                listaActual.add(ItemCarrito(producto, cantidadFinal))
            }
        }

        _carrito.value = listaActual.toList()
    }

    fun incrementarCantidad(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val item = listaActual[index]
            if (item.cantidad < item.producto.stock) {
                listaActual[index] = item.copy(cantidad = item.cantidad + 1)
                _carrito.value = listaActual.toList()
            }
        }
    }

    fun decrementarCantidad(producto: Producto) {
        val listaActual = _carrito.value.toMutableList()
        val index = listaActual.indexOfFirst { it.producto.id == producto.id }

        if (index != -1) {
            val item = listaActual[index]
            if (item.cantidad > 1) {
                listaActual[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                listaActual.removeAt(index)
            }
            _carrito.value = listaActual.toList()
        }
    }

    fun eliminarProducto(producto: Producto) {
        _carrito.value = _carrito.value.filter { it.producto.id != producto.id }
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }
}
