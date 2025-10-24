package com.example.magiceast.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegistroViewModel : ViewModel(){

    var nombre = mutableStateOf("")
    var apellidos = mutableStateOf("")
    var email = mutableStateOf("")
    var contrasena = mutableStateOf("")
    var fechaNacimiento = mutableStateOf("")

    var ofertas = mutableStateOf(false)
    var noticias =  mutableStateOf(false)

    var nombreError = mutableStateOf<String?>(null)
    var apellidosError = mutableStateOf<String?>(null)
    var emailError = mutableStateOf<String?>(null)
    var contrasenaError = mutableStateOf<String?>(null)
    var fechaNacimientoError = mutableStateOf<String?>(null)

    // Regex para email válido
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")

    // Regex para contraseña: mínimo 8, 1 mayúscula, 1 número, 1 símbolo
    private val contrasenaRegex =Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    //Regex para fecha naciemiento
    private val fechaNacimientoRegex = Regex("\\d{2}/\\d{2}/\\d{4}")

    fun validar(): Boolean{
        var valid = true
        //validar nombre
        if (nombre.value.isBlank()){
            nombreError.value = "Ingresa tu nombre"
            valid = false
        }else nombreError.value = null
        //validar apellidos
        if (apellidos.value.isBlank()){
            apellidosError.value = "Ingresa tus apellidos"
            valid = false
        }else apellidosError.value = null
        //validar correo
        if (email.value.isBlank()){
            emailError.value = "Ingresa un correo"
            valid = false
        }else emailError.value = null

        if (!emailRegex.matches(email.value)){
            emailError.value = "Correo invalido"
            valid = false
        }else emailError.value = null
        //validar contraseña
        if (contrasena.value.isBlank()){
            contrasenaError.value = "La contraseña no puede estar vacía"
            valid = false
        }else contrasenaError.value = null
        if (!contrasenaRegex.matches(contrasena.value)){
            contrasenaError.value = "Debe tener 8+ caracteres, 1 mayúscula, 1 número y 1 símbolo"
            valid = false
        }else contrasenaError.value = null
        //validar Fecha naciemiento
        if(fechaNacimiento.value.isNotBlank() && !fechaNacimiento.value.matches(fechaNacimientoRegex)){
            fechaNacimientoError.value = "Fecha de nacimiento no valida (DD/MM/AAAA)"
            valid = false
        }else fechaNacimientoError.value = null

        return valid
    }

    fun registro(): Boolean {
        return validar()
    }

    fun clearErrors(){
        nombreError.value = null
        apellidosError.value = null
        emailError.value = null
        contrasenaError.value = null
        fechaNacimientoError.value = null

    }

}