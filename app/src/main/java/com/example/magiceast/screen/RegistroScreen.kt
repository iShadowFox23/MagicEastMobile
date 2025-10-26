package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.filled.CalendarToday
import com.example.magiceast.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onBack: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    onRegister: () -> Unit = {}
){
    val viewModel: RegistroViewModel = viewModel()

    val nombre by viewModel.nombre
    val apellidos by viewModel.apellidos
    val email by viewModel.email
    val contrasena by viewModel.contrasena
    val fechaNacimiento by viewModel.fechaNacimiento
    val rut by viewModel.rut
    val nombreError by viewModel.nombreError
    val apellidosError by viewModel.apellidosError
    val emailError by viewModel.emailError
    val contrasenaError by viewModel.contrasenaError
    val fechaNacimientoError by viewModel.fechaNacimientoError
    val rutError by viewModel.rutError
    var showSuccessDialog by remember { mutableStateOf(false) }

    var contrasenaVisible by remember { mutableStateOf(false)}

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
            onValueChange = { viewModel.nombre.value = it },
            label = { Text("Nombre", color = Color.Gray) },
            isError = nombreError != null,
            supportingText = { nombreError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(12.dp))

        // Apellidos
        OutlinedTextField(
            value = apellidos,
            onValueChange = { viewModel.apellidos.value = it },
            label = { Text("Apellidos", color = Color.Gray) },
            isError = apellidosError != null,
            supportingText = { apellidosError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(12.dp))

        // Rut
        OutlinedTextField(
            value = rut,
            onValueChange = { viewModel.rut.value = it },
            label = { Text("RUT", color = Color.Gray) },
            isError = rutError != null,
            supportingText = { rutError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(12.dp))

        // Correo
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Correo electrónico", color = Color.Gray) },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(12.dp))

        // Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { viewModel.contrasena.value = it },
            label = { Text("Contraseña", color = Color.Gray) },
            isError = contrasenaError != null,
            supportingText = { contrasenaError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            trailingIcon = {
                val image = if (contrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (contrasenaVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray
                    )
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        // Fecha de nacimiento (opcional)
        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { viewModel.fechaNacimiento.value = it },
            label = { Text("Fecha de nacimiento (opcional)", color = Color.Gray) },
            isError = fechaNacimientoError != null,
            supportingText = { fechaNacimientoError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            trailingIcon = {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "Calendario", tint = Color.Gray)
            },
            modifier = Modifier.width(400.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(16.dp))

        // Checkboxes
        Row(
            modifier = Modifier.width(400.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.ofertas.value,
                onCheckedChange = { viewModel.ofertas.value = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF720B0B))
            )
            Text("Recibir ofertas exclusivas", color = Color.White)
        }

        Row(
            modifier = Modifier.width(400.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.noticias.value,
                onCheckedChange = { viewModel.noticias.value = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF720B0B))
            )
            Text("Suscribirse a las noticias", color = Color.White)
        }

        Spacer(Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                if (viewModel.validar()) {
                    showSuccessDialog = true

                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            modifier = Modifier.width(250.dp)
        ) {
            Text("Crear cuenta", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // Volver
        TextButton(onClick = onBack) {
            Text("Volver al inicio", color = Color.Gray)
        }
    }
    //Popup de cuenta registrada con exito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
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