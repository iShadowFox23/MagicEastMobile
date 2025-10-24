package com.example.magiceast.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import com.example.magiceast.model.Producto


class ProductoRepository {

    fun obtenerProductosDesdeAssets(context: Context, filename: String = "productos.json"): List<Producto> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Producto>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}