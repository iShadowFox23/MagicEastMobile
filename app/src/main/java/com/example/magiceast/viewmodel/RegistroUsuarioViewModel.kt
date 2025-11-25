package com.example.magiceast.viewmodel

import androidx.lifecycle.*
import com.example.magiceast.data.repository.UsuarioRepository
import com.example.magiceast.model.Usuario
import kotlinx.coroutines.launch

class RegistroUsuarioViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    private val _usuarios = MutableLiveData<List<Usuario>>(emptyList())
    val usuarios: LiveData<List<Usuario>> get() = _usuarios

    private val _cargando = MutableLiveData<Boolean>(false)
    val cargando: LiveData<Boolean> get() = _cargando

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    fun listarUsuarios() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val lista = repository.listarUsuarios()
            _usuarios.value = lista ?: emptyList()

            _cargando.value = false
        }
    }

    fun eliminarUsuario(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val eliminado = repository.eliminarUsuario(id)

            if (eliminado) {
                listarUsuarios()
            } else {
                _error.value = "No se pudo eliminar el usuario"
                _cargando.value = false
            }
        }
    }
}
