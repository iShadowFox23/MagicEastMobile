import com.example.magiceast.data.repository.UsuarioRepository
import com.example.magiceast.model.Usuario
import com.example.magiceast.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var repository: UsuarioRepository
    private lateinit var viewModel: LoginViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        viewModel = LoginViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validate con campos vacíos da error`() {
        viewModel.email.value = ""
        viewModel.password.value = ""

        val valido = viewModel.validate()

        assertFalse(valido)
        assertEquals("El correo no puede estar vacío", viewModel.emailError.value)
        assertEquals("La contraseña no puede estar vacía", viewModel.passwordError.value)
    }

    @Test
    fun `validate con formato incorrecto da mensajes de error`() {
        viewModel.email.value = "correo_invalido"
        viewModel.password.value = "abc"

        val valido = viewModel.validate()

        assertFalse(valido)
        assertEquals("Correo inválido", viewModel.emailError.value)
        assertEquals("Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo", viewModel.passwordError.value)
    }

    @Test
    fun `login exitoso como admin actualiza loginSuccessAdmin`() = runTest {
        viewModel.email.value = "magiceast@admin.cl"
        viewModel.password.value = "Admin123!"

        viewModel.login()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.loginSuccessAdmin.first())
        assertNull(viewModel.loginError.first())
    }

    @Test
    fun `login con correo no registrado devuelve mensaje de error`() = runTest {
        viewModel.email.value = "noexiste@correo.cl"
        viewModel.password.value = "Admin123!"

        coEvery { repository.obtenerUsuarioPorEmail("noexiste@correo.cl") } returns null

        viewModel.login()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Correo no registrado", viewModel.loginError.first())
        assertFalse(viewModel.loginSuccessUser.first())
    }

    @Test
    fun `login con contraseña incorrecta da error`() = runTest {
        viewModel.email.value = "usuario@correo.cl"
        viewModel.password.value = "Password123!"

        val usuario = Usuario(id = 1, nombre = "Test", email = "usuario@correo.cl", contrasena = "OtraClave123!", direccion = "Callefalsa 123, comunafalsa")

        coEvery { repository.obtenerUsuarioPorEmail("usuario@correo.cl") } returns usuario

        viewModel.login()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Contraseña incorrecta", viewModel.loginError.first())
        assertFalse(viewModel.loginSuccessUser.first())
    }

    @Test
    fun `login exitoso como usuario actualiza loginSuccessUser`() = runTest {
        viewModel.email.value = "user@correo.cl"
        viewModel.password.value = "User123!"

        val usuario = Usuario(id = 2, nombre = "Normal", email = "user@correo.cl", contrasena = "User123!", direccion = "Callefalsa 123, comunafalsa")

        coEvery { repository.obtenerUsuarioPorEmail("user@correo.cl") } returns usuario

        viewModel.login()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.loginSuccessUser.first())
        assertNull(viewModel.loginError.first())
    }

    @Test
    fun `login lanza excepción y actualiza error`() = runTest {
        viewModel.email.value = "error@correo.cl"
        viewModel.password.value = "Admin123!"

        coEvery { repository.obtenerUsuarioPorEmail(any()) } throws RuntimeException("Falló")

        viewModel.login()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Error al conectar con el servidor", viewModel.loginError.first())
    }
}
