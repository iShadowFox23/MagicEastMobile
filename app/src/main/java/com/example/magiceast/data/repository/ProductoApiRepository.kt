package com.example.magiceast.data.repository

import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.remote.ProductoApiClient
import com.example.magiceast.model.Producto

class ProductoApiRepository {

    suspend fun getProductos(): Result<List<Producto>> {
        return runCatching {
            val dtoList = ProductoApiClient.api.getProductos()
            dtoList.map { it.toDomain() }
        }
    }
}