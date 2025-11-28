import android.content.Context
import android.net.Uri
import com.example.magiceast.data.repository.ProductoApiRepository
import com.example.magiceast.model.Producto
import com.example.magiceast.viewmodel.AdminViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    private lateinit var repository: ProductoApiRepository
    private lateinit var viewModel: AdminViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val producto = Producto(
        id = 1,
        nombre = "Mazo",
        precio = 60000,
        precioAntiguo = 0,
        descuento = 0,
        stock = 10,
        categoria = "Mazo",
        imagen = "Mazo.jpg",
        descripcion = "Mazo preconstruido",
        estado = "nuevo"
    )

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = AdminViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos actualiza productos y limpia error`() = runTest {
        coEvery { repository.listarProductos() } returns listOf(producto)

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(producto), viewModel.productos.first())
        assertNull(viewModel.error.first())
    }


    @Test
    fun `agregarProducto agrega producto correctamente`() = runTest {
        val context = mockk<Context>()
        val uri = mockk<Uri>()
        coEvery { repository.crearProductoConImagen(context, producto, uri) } returns producto

        viewModel.agregarProducto(context, producto, uri)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.productos.first().contains(producto))
        assertNull(viewModel.error.first())
    }


    @Test
    fun `editarProducto actualiza producto en lista`() = runTest {
        viewModel = AdminViewModel(repository).apply {
            val productosField = this::class.java.getDeclaredField("_productos")
            productosField.isAccessible = true
            productosField.set(this, kotlinx.coroutines.flow.MutableStateFlow(listOf(producto)))
        }

        val actualizado = producto.copy(nombre = "Mazo Actualizado")
        coEvery { repository.actualizarProducto(actualizado) } returns actualizado

        viewModel.editarProducto(actualizado)
        testDispatcher.scheduler.advanceUntilIdle()

        val actualizadoEnLista = viewModel.productos.first().find { it.id == actualizado.id }
        assertEquals("Mazo Actualizado", actualizadoEnLista?.nombre)
    }



    @Test
    fun `eliminarProducto elimina producto de la lista`() = runTest {
        viewModel = AdminViewModel(repository).apply {
            val productosField = this::class.java.getDeclaredField("_productos")
            productosField.isAccessible = true
            productosField.set(this, kotlinx.coroutines.flow.MutableStateFlow(listOf(producto)))
        }

        coEvery { repository.eliminarProducto(producto.id) } returns true

        viewModel.eliminarProducto(producto.id)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.productos.first().isEmpty())
    }
}
