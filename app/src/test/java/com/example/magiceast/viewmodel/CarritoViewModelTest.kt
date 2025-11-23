package com.example.magiceast.viewmodel

import com.example.magiceast.data.model.Carta
import com.example.magiceast.model.Producto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    private lateinit var viewModel: CarritoViewModel

    @Before
    fun setup() {
        viewModel = CarritoViewModel()
    }

    private fun crearProducto(
        id: Int = 1,
        stock: Int = 5,
        nombre: String = "Producto test",
        precio: Int = 1000
    ) = Producto(
        id = id,
        nombre = nombre,
        precio = precio,
        precioAntiguo = precio,
        descuento = 0,
        stock = stock,
        categoria = "Test",
        imagen = "",
        descripcion = "",
        estado = "Nuevo"
    )

    private fun crearCarta(
        id: String? = "carta123",
        name: String? = "Black Lotus",
        manaCost: String? = "{0}",
        typeLine: String? = "Legendary Artifact",
        imageUrl: String? = "http://example.com/card.png",
        valor: Int = 500,
        stock: Int = 4
    ) = Carta(
        id = id,
        name = name,
        manaCost = manaCost,
        typeLine = typeLine,
        imageUrl = imageUrl,
        valor = valor,
        stock = stock
    )


    @Test
    fun `agregarProducto agrega nuevo item`() = runTest {
        val p = crearProducto(1)

        viewModel.agregarProducto(p)

        val carrito = viewModel.carrito.value
        assertEquals(1, carrito.size)
        assertEquals(1, carrito[0].cantidad)
        assertEquals(p.id, carrito[0].producto.id)
    }

    @Test
    fun `agregarProducto incrementa si ya existe`() = runTest {
        val p = crearProducto(1)

        viewModel.agregarProducto(p)
        viewModel.agregarProducto(p)

        val carrito = viewModel.carrito.value
        assertEquals(1, carrito.size)
        assertEquals(2, carrito[0].cantidad)
    }

    @Test
    fun `incrementarCantidad aumenta hasta stock`() = runTest {
        val p = crearProducto(id = 1, stock = 2)

        viewModel.agregarProducto(p)
        viewModel.incrementarCantidad(p)
        viewModel.incrementarCantidad(p) // no debe pasar de 2

        val carrito = viewModel.carrito.value
        assertEquals(2, carrito[0].cantidad)
    }

    @Test
    fun `decrementarCantidad reduce y elimina si llega a cero`() = runTest {
        val p = crearProducto(1)

        viewModel.agregarProducto(p)
        viewModel.incrementarCantidad(p) // cantidad = 2

        viewModel.decrementarCantidad(p) // 2 -> 1
        assertEquals(1, viewModel.carrito.value[0].cantidad)

        viewModel.decrementarCantidad(p) // 1 -> eliminado
        assertTrue(viewModel.carrito.value.isEmpty())
    }

    @Test
    fun `eliminarProducto remueve el producto del carrito`() = runTest {
        val p = crearProducto(1)

        viewModel.agregarProducto(p)
        viewModel.eliminarProducto(p)

        assertTrue(viewModel.carrito.value.isEmpty())
    }

    @Test
    fun `limpiarCarrito deja vacio`() = runTest {
        viewModel.agregarProducto(crearProducto(1))
        viewModel.agregarProducto(crearProducto(2))

        viewModel.limpiarCarrito()

        assertTrue(viewModel.carrito.value.isEmpty())
    }

    @Test
    fun `agregarCartaAlCarrito agrega carta convertida a producto`() = runTest {
        val carta = crearCarta(stock = 3, valor = 700)

        viewModel.agregarCartaAlCarrito(carta, cantidad = 2)

        val carrito = viewModel.carrito.value
        assertEquals(1, carrito.size)

        val item = carrito[0]


        assertEquals(carta.id.hashCode(), item.producto.id)
        assertEquals(carta.valor, item.producto.precio)
        assertEquals("Cartas MTG", item.producto.categoria)
        assertEquals(carta.name, item.producto.nombre)
        assertEquals(carta.typeLine, item.producto.descripcion)
        assertEquals(2, item.cantidad)
    }

    @Test
    fun `agregarCartaAlCarrito respeta el stock`() = runTest {
        val carta = crearCarta(stock = 2)

        viewModel.agregarCartaAlCarrito(carta, cantidad = 10)

        val carrito = viewModel.carrito.value
        assertEquals(1, carrito.size)
        assertEquals(2, carrito[0].cantidad)
    }

    @Test
    fun `agregarCartaAlCarrito aumenta cantidad si ya existe`() = runTest {
        val carta = crearCarta(stock = 4)

        viewModel.agregarCartaAlCarrito(carta, cantidad = 1)
        viewModel.agregarCartaAlCarrito(carta, cantidad = 3)

        val carrito = viewModel.carrito.value
        assertEquals(1, carrito.size)
        assertEquals(4, carrito[0].cantidad)
    }
}
