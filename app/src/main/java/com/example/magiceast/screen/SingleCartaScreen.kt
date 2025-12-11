// app/src/main/java/com/example/magiceast/screen/SingleCartaScreen.kt
package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.viewmodel.CarritoViewModel
import com.example.magiceast.viewmodel.SingleCartaViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import android.icu.text.NumberFormat
import android.icu.util.ULocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleCartaScreen(
    navController: NavController,
    cartaId: String,
    carritoViewModel: CarritoViewModel,
    viewModel: SingleCartaViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState
    val clpFormatter = NumberFormat.getCurrencyInstance(ULocale("es_CL"))

    LaunchedEffect(cartaId) {
        if (cartaId.isNotBlank()) {
            viewModel.loadCard(cartaId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                state.loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar la carta",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.error,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                state.carta != null -> {
                    val carta = state.carta

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!carta.imageUrl.isNullOrBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(carta.imageUrl),
                                contentDescription = carta.name,
                                modifier = Modifier.height(260.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .height(260.dp)
                                    .fillMaxWidth()
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = carta.name ?: "Sin nombre",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = carta.typeLine ?: "",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (!carta.manaCost.isNullOrBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Coste de maná: ${carta.manaCost}",
                                color = Color(0xFFCCCCCC),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Set: ${carta.setName ?: "Desconocido"}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }
            }
        }
    }
}
