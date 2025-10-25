package com.example.magiceast.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.magiceast.model.Producto
import com.example.magiceast.repository.ProductoRepository

class AdminViewModel : ViewModel() {


    val productos get() = ProductoRepository.productos

    fun cargarProductos(context: Context) {
        ProductoRepository.cargarProductos(context)
    }

    fun eliminarProducto(id: Int) {
        ProductoRepository.eliminarProducto(id)
    }

    fun actualizarStock(id: Int, nuevoStock: Int) {
        ProductoRepository.actualizarStock(id, nuevoStock)
    }

    fun cambiarEstado(id: Int, nuevoEstado: String) {
        ProductoRepository.cambiarEstado(id, nuevoEstado)
    }

    fun editarProducto(id: Int, nuevoNombre: String, nuevoPrecio: Int) {
        ProductoRepository.editarProducto(id, nuevoNombre, nuevoPrecio)
    }

    fun agregarProducto(producto: Producto) {
        ProductoRepository.agregarProducto(producto)
    }
}
