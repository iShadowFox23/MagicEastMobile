package com.example.magiceast.data.repository

import android.content.Context
import com.example.magiceast.data.model.toDomain
import com.example.magiceast.data.model.toDto
import com.example.magiceast.data.remote.ProductoApiClient
import com.example.magiceast.data.remote.ProductoApiService
import com.example.magiceast.data.remote.dto.ProductoApiDto
import com.example.magiceast.model.Producto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoApiRepositoryTest {

    private val producto = Producto(
        id = 1,
        nombre = "Carta de Prueba",
        precio = 1000,
        precioAntiguo = 0,
        descuento = 0,
        stock = 5,
        categoria = "Test",
        imagen = "imagen.jpg",
        descripcion = "Carta de test",
        estado = "nuevo"
    )

    private lateinit var repository: ProductoApiRepository
    private lateinit var mockApi: ProductoApiService

    @Before
    fun setUp() {
        mockApi = mockk()
        mockkObject(ProductoApiClient)
        every { ProductoApiClient.api } returns mockApi
        repository = ProductoApiRepository()
    }

    @Test
    fun `listarProductos retorna lista correctamente`() = runTest {
        coEvery { mockApi.listarProductos() } returns listOf(producto.toDto())

        val resultado = repository.listarProductos()

        assertEquals(1, resultado.size)
        assertEquals(producto.nombre, resultado[0].nombre)
    }

    @Test
    fun `obtenerProducto retorna producto si es exitoso`() = runTest {
        val response: Response<ProductoApiDto> = Response.success(producto.toDto())
        coEvery { mockApi.obtenerProducto(producto.id) } returns response

        val resultado = repository.obtenerProducto(producto.id)

        assertEquals(producto.nombre, resultado?.nombre)
    }

    @Test
    fun `crearProducto retorna producto creado`() = runTest {
        val response: Response<ProductoApiDto> = Response.success(producto.toDto())
        coEvery { mockApi.crearProducto(any()) } returns response

        val resultado = repository.crearProducto(producto)

        assertEquals(producto.nombre, resultado?.nombre)
    }

    @Test
    fun `actualizarProducto retorna producto actualizado`() = runTest {
        val actualizado = producto.copy(nombre = "Actualizado")
        val response: Response<ProductoApiDto> = Response.success(actualizado.toDto())
        coEvery { mockApi.actualizarProducto(actualizado.id, any()) } returns response

        val resultado = repository.actualizarProducto(actualizado)

        assertEquals("Actualizado", resultado?.nombre)
    }

    @Test
    fun `eliminarProducto retorna true si es exitoso`() = runTest {
        coEvery { mockApi.eliminarProducto(producto.id) } returns Response.success(Unit)

        val resultado = repository.eliminarProducto(producto.id)

        assertTrue(resultado)
    }

    @Test
    fun `crearProductoConImagen retorna null si uri es null`() = runTest {
        val context = mockk<Context>()
        val resultado = repository.crearProductoConImagen(context, producto, null)
        assertNull(resultado)
    }
}
