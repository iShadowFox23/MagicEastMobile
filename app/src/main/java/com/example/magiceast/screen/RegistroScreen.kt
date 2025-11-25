package com.example.magiceast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magiceast.data.remote.dto.UsuarioApiDto
import com.example.magiceast.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onBack: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    val viewModel: RegistroViewModel = viewModel()

    // Estados locales para inputs
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    var contrasenaVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val usuarioRegistrado by viewModel.usuarioRegistrado.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cuando el backend responda OK -> mostrar popup
    LaunchedEffect(usuarioRegistrado) {
        if (usuarioRegistrado != null) {
            showSuccessDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear cuenta",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre", color = Color.Gray) },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )
        Spacer(Modifier.height(16.dp))

        // Dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección", color = Color.Gray) },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )
        Spacer(Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico", color = Color.Gray) },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )
        Spacer(Modifier.height(16.dp))

        // Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña", color = Color.Gray) },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                    Icon(
                        imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )
        Spacer(Modifier.height(30.dp))

        // Botón registrar
        Button(
            onClick = {
                val dto = UsuarioApiDto(
                    nombre = nombre,
                    email = email,
                    direccion = direccion,
                    contrasena = contrasena
                )
                viewModel.registrarUsuario(dto)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            modifier = Modifier.width(250.dp)
        ) {
            Text("Crear cuenta", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // Error backend
        error?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = onBack) {
            Text("Volver al inicio", color = Color.Gray)
        }
    }

    // Éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onRegisterSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B))
                ) {
                    Text("Aceptar", color = Color.White)
                }
            },
            title = { Text("Cuenta creada con éxito", color = Color.White) },
            text = { Text("Tu cuenta ha sido registrada correctamente.", color = Color.LightGray) },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}
