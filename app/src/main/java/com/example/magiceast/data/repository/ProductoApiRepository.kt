package com.example.magiceast.data.repository

import android.content.Context
import android.net.Uri
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.model.toDto
import com.example.magiceast.data.remote.ProductoApiClient
import com.example.magiceast.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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


    suspend fun crearProductoConImagen(
        context: Context,
        producto: Producto,
        imageUri: Uri?
    ): Producto? {

        if (imageUri == null) return null

        return withContext(Dispatchers.IO) {
            try {
                // Convertir Uri → archivo temporal
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val tempFile = File.createTempFile("img_", ".tmp", context.cacheDir)
                tempFile.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }

                val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagenPart = MultipartBody.Part.createFormData(
                    "imagen",
                    "img_${System.currentTimeMillis()}.jpg",
                    requestFile
                )

                val nombreBody = producto.nombre.toRequestBody("text/plain".toMediaTypeOrNull())
                val marcaBody = "MagicEast".toRequestBody("text/plain".toMediaTypeOrNull())
                val categoriasBody = producto.categoria.toRequestBody("text/plain".toMediaTypeOrNull())
                val precioBody = producto.precio.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val stockBody = producto.stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val descripcionBody = (producto.descripcion ?: "")
                    .toRequestBody("text/plain".toMediaTypeOrNull())

                val response = ProductoApiClient.api.crearProductoConImagen(
                    nombreBody,
                    marcaBody,
                    categoriasBody,
                    precioBody,
                    stockBody,
                    descripcionBody,
                    imagenPart
                )

                if (response.isSuccessful) response.body()?.toDomain() else null

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
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
