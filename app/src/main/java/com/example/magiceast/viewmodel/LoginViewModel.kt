package com.example.magiceast.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.repository.UsuarioRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(
    private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    var emailError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)

    private val _loginSuccessUser = MutableStateFlow(false)
    val loginSuccessUser: StateFlow<Boolean> get() = _loginSuccessUser

    private val _loginSuccessAdmin = MutableStateFlow(false)
    val loginSuccessAdmin: StateFlow<Boolean> get() = _loginSuccessAdmin

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    private val passwordRegex =
        Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")


    fun validate(): Boolean {
        var valid = true

        if (email.value.isBlank()) {
            emailError.value = "El correo no puede estar vacío"
            valid = false
        } else if (!emailRegex.matches(email.value)) {
            emailError.value = "Correo inválido"
            valid = false
        } else {
            emailError.value = null
        }

        if (password.value.isBlank()) {
            passwordError.value = "La contraseña no puede estar vacía"
            valid = false
        } else if (!passwordRegex.matches(password.value)) {
            passwordError.value = "Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo"
            valid = false
        } else {
            passwordError.value = null
        }

        return valid
    }


    fun login() {
        if (!validate()) return

        if (email.value == "magiceast@admin.cl" && password.value == "Admin123!") {
            _loginSuccessAdmin.value = true
            _loginError.value = null
            return
        }

        viewModelScope.launch {
            try {
                val usuario = repository.obtenerUsuarioPorEmail(email.value)

                if (usuario == null) {
                    _loginError.value = "Correo no registrado"
                    return@launch
                }

                if (usuario.contrasena == password.value) {
                    _loginSuccessUser.value = true
                    _loginError.value = null
                } else {
                    _loginError.value = "Contraseña incorrecta"
                }

            } catch (e: Exception) {
                _loginError.value = "Error al conectar con el servidor"
            }
        }
    }

    fun clearErrors() {
        emailError.value = null
        passwordError.value = null
        _loginError.value = null
    }
}
