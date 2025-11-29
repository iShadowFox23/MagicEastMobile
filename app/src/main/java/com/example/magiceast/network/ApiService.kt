package com.example.magiceast.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// DTO que enviamos al backend
data class ItemCompraDTO(
    val productoId: Long,
    val cantidad: Int
)

data class CompraDTO(
    val items: List<ItemCompraDTO>
)

// Interface de endpoints
interface ApiService {

    @POST("api/checkout")   // SIN slash inicial (retrofit lo maneja mejor)
    suspend fun procesarCompra(@Body compra: CompraDTO): Response<String>
}
