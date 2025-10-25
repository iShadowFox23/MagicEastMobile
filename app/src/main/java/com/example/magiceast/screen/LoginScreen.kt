package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.magiceast.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    onLoginAdmin: () -> Unit = {},
    onLoginUser: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    val viewModel: LoginViewModel = viewModel()

    val email by viewModel.email
    val password by viewModel.password
    val emailError by viewModel.emailError
    val passwordError by viewModel.passwordError
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .offset(y = (-30).dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("file:///android_asset/images/banner.webp"),
                contentDescription = "Banner principal",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(
                        color = Color(0xAA000000),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Inicio de Sesión",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de correo
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Correo electrónico", color = Color.Gray) },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier
                .width(400.dp)
                .align(Alignment.CenterHorizontally),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
        )

        Spacer(Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Contraseña", color = Color.Gray) },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it, color = Color.Red, fontSize = 12.sp) } },
            modifier = Modifier
                .width(400.dp)
                .align(Alignment.CenterHorizontally),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray
                    )
                }
            }
        )

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = {
                if (viewModel.validate()) {
                    if (viewModel.isAdmin()) {
                        onLoginAdmin()
                    } else {
                        onLoginUser()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            modifier = Modifier
                .width(250.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        //Botón de registro
        Button(
            onClick = onRegister,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            modifier = Modifier
                .width(250.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Registrarse", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))


        TextButton(onClick = onBack) {
            Text("Volver al inicio", color = Color.Gray)
        }
    }
}
