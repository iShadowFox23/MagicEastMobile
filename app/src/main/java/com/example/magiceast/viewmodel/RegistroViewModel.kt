package com.example.magiceast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.data.repository.UsuarioRepository
import com.example.magiceast.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _usuarioRegistrado = MutableStateFlow<Usuario?>(null)
    val usuarioRegistrado: StateFlow<Usuario?> get() = _usuarioRegistrado

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun registrarUsuario(usuarioDto: UsuarioApiDto) {
        viewModelScope.launch {
            val result = repository.registrarUsuario(usuarioDto)

            if (result != null) {
                _usuarioRegistrado.value = result
                _error.value = null
            } else {
                _error.value = "No se pudo registrar el usuario"
            }
        }

    }
}
