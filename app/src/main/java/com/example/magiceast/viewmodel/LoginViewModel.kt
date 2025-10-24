package com.example.magiceast.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var emailError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)

    // Regex para email válido
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")

    // Regex para contraseña: mínimo 8, 1 mayúscula, 1 número, 1 símbolo
    private val passwordRegex =
        Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    fun validate(): Boolean {
        var isValid = true

        emailError.value = when {
            email.value.isBlank() -> {
                isValid = false
                "El correo no puede estar vacío"
            }
            !emailRegex.matches(email.value) -> {
                isValid = false
                "Formato de correo inválido"
            }
            else -> null
        }

        passwordError.value = when {
            password.value.isBlank() -> {
                isValid = false
                "La contraseña no puede estar vacía"
            }
            !passwordRegex.matches(password.value) -> {
                isValid = false
                "Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo"
            }
            else -> null
        }

        return isValid
    }

    fun login(): Boolean {
        return validate()
    }

    fun clearErrors() {
        emailError.value = null
        passwordError.value = null
    }
}
