package com.example.magiceast.repository

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.magiceast.model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProductoRepository {

    //Lista observable compartida entre catálogo y back office
    private val _productos = mutableStateListOf<Producto>()
    val productos: List<Producto> get() = _productos


    fun cargarProductos(context: Context, filename: String = "productos.json") {
        if (_productos.isNotEmpty()) return

        try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Producto>>() {}.type
            val lista = Gson().fromJson<List<Producto>>(json, type)
            _productos.clear()
            _productos.addAll(lista)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun obtenerProductos(): List<Producto> = _productos


    fun obtenerProductoPorId(id: Int): Producto? = _productos.find { it.id == id }


    fun editarProducto(id: Int, nuevoNombre: String, nuevoPrecio: Int) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            val producto = _productos[index]
            _productos[index] = producto.copy(
                nombre = nuevoNombre.ifBlank { producto.nombre },
                precio = if (nuevoPrecio > 0) nuevoPrecio else producto.precio
            )
        }
    }


    fun eliminarProducto(id: Int) {
        _productos.removeIf { it.id == id }
    }


    fun actualizarStock(id: Int, nuevoStock: Int) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            val producto = _productos[index]
            _productos[index] = producto.copy(stock = nuevoStock)
        }
    }


    fun cambiarEstado(id: Int, nuevoEstado: String) {
        val index = _productos.indexOfFirst { it.id == id }
        if (index != -1) {
            val producto = _productos[index]
            _productos[index] = producto.copy(estado = nuevoEstado)
        }
    }


    fun agregarProducto(producto: Producto) {
        if (_productos.any { it.id == producto.id }) {
            println("Producto con ID ${producto.id} ya existe, no se agregó.")
            return
        }
        _productos.add(producto)
    }


    fun limpiar() {
        _productos.clear()
    }
}
