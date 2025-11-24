package com.example.magiceast.data.remote

import com.example.magiceast.data.remote.dto.ProductoApiDto
import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {

    @GET("productos")
    suspend fun listarProductos(): List<ProductoApiDto>

    @GET("productos/{id}")
    suspend fun obtenerProducto(@Path("id") id: Int): Response<ProductoApiDto>

    @POST("productos")
    suspend fun crearProducto(@Body producto: ProductoApiDto): Response<ProductoApiDto>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: ProductoApiDto
    ): Response<ProductoApiDto>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Unit>
}
