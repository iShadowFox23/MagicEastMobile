package com.example.magiceast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.model.Producto
import com.example.magiceast.data.model.Carta
import com.example.magiceast.network.RetrofitInstance
import com.example.magiceast.network.CompraDTO
import com.example.magiceast.network.ItemCompraDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    // -------------------------------------------------------
    // NUEVO: Confirmar compra y enviar al backend
    // -------------------------------------------------------
    fun confirmarCompra(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {

            try {
                val itemsDTO = _carrito.value.map {
                    ItemCompraDTO(
                        productoId = it.producto.id.toLong(),
                        cantidad = it.cantidad
                    )
                }

                val compraDTO = CompraDTO(items = itemsDTO)

                val response = RetrofitInstance.api.procesarCompra(compraDTO)

                if (response.isSuccessful) {
                    limpiarCarrito()
                    onSuccess()
                } else {
                    onError("Error en el backend: ${response.code()}")
                }

            } catch (e: Exception) {
                onError("No se pudo procesar la compra: ${e.message}")
            }
        }
    }
}
