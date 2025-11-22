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

                    // Cantidad seleccionada, no puede pasar del stock ni bajar de 1 (si hay stock)
                    var cantidad by remember(carta.id) {
                        mutableIntStateOf(if (carta.stock > 0) 1 else 0)
                    }

                    val tieneStock = carta.stock > 0

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

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "Valor: ${clpFormatter.format(carta.valor)}",
                            color = Color(0xFF00E676),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Stock disponible: ${carta.stock}",
                            color = if (tieneStock) Color(0xFF00E676) else Color.Red,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(16.dp))

                        if (tieneStock) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        if (cantidad > 1) cantidad -= 1
                                    },
                                    enabled = cantidad > 1
                                ) {
                                    Text("-")
                                }

                                Text(
                                    text = cantidad.toString(),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                OutlinedButton(
                                    onClick = {
                                        if (cantidad < carta.stock) cantidad += 1
                                    },
                                    enabled = cantidad < carta.stock
                                ) {
                                    Text("+")
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                        } else {
                            Spacer(Modifier.height(32.dp))
                        }


                        Button(
                            onClick = {
                                if (tieneStock && cantidad > 0) {
                                    carritoViewModel.agregarCartaAlCarrito(carta, cantidad)

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Carta agregada al carrito",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            },
                            enabled = tieneStock && cantidad > 0,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tieneStock) Color(0xFF720B0B) else Color(0xFF444444),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF444444),
                                disabledContentColor = Color.LightGray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = if (tieneStock) "Agregar al carrito" else "Sin stock",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
