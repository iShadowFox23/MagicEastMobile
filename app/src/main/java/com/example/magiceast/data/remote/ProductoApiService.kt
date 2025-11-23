package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.ProductoApiDto
import retrofit2.http.GET

interface ProductoApiService {


    @GET("productos")
    suspend fun getProductos(): List<ProductoApiDto>
}