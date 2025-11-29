package com.example.magiceast.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class ItemCompraDTO(
    val productoId: Long,
    val cantidad: Int
)

data class CompraDTO(
    val items: List<ItemCompraDTO>
)


interface ApiService {

    @POST("api/checkout")
    suspend fun procesarCompra(@Body compra: CompraDTO): Response<String>

}
