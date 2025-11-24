package com.example.magiceast.data.repository

import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.model.toDto
import com.example.magiceast.data.remote.ProductoApiClient
import com.example.magiceast.model.Producto

class ProductoApiRepository {

    suspend fun listarProductos(): List<Producto> {
        return try {
            ProductoApiClient.api.listarProductos().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun obtenerProducto(id: Int): Producto? {
        return try {
            val response = ProductoApiClient.api.obtenerProducto(id)
            if (response.isSuccessful) response.body()?.toDomain() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun crearProducto(producto: Producto): Producto? {
        return try {
            val response = ProductoApiClient.api.crearProducto(producto.toDto())
            if (response.isSuccessful) response.body()?.toDomain() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun actualizarProducto(producto: Producto): Producto? {
        return try {
            val response = ProductoApiClient.api.actualizarProducto(producto.id, producto.toDto())
            if (response.isSuccessful) response.body()?.toDomain() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun eliminarProducto(id: Int): Boolean {
        return try {
            ProductoApiClient.api.eliminarProducto(id).isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
