package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun MainScreen(
    onGoToCatalogo: () -> Unit,
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onCardClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("file:///android_asset/images/banner.webp"),
                contentDescription = "Banner principal",
                modifier = Modifier.fillMaxSize(),
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
                    text = "Bienvenido a MagicEast",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Button(
            onClick = onGoToCatalogo,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Ver Catálogo",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onRegisterClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Registrarse",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onCardClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Cartas",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("file:///android_asset/images/avatar.jpeg"),
                contentDescription = "Decoración",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xAA000000),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = 800f
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "© 2025 MagicEast — Todos los derechos reservados.",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}
