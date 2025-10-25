package com.example.magiceast.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.magiceast.model.Producto
import com.example.magiceast.repository.ProductoRepository

class CatalogoViewModel : ViewModel() {


    val productos get() = ProductoRepository.productos

    fun cargarProductos(context: Context) {

        ProductoRepository.cargarProductos(context)
    }

    fun buscarProductoPorId(id: Int): Producto? =
        ProductoRepository.obtenerProductoPorId(id)
}
