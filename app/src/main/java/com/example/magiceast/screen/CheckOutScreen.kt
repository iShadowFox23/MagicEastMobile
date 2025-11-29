package com.example.magiceast.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.magiceast.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    onPurchaseComplete: (Boolean) -> Unit
) {
    val carrito by carritoViewModel.carrito.collectAsState()
    val total = carrito.sumOf { it.producto.precio * it.cantidad }
    val opcionesEnvio = listOf("Estándar (3-5 días)" to 5000, "Express (1-2 días)" to 10000)
    var envioSeleccionado by remember { mutableStateOf(opcionesEnvio[0]) }
    val totalConEnvio = total + envioSeleccionado.second
    var loading by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var compraExitosa by remember { mutableStateOf(false) }

    if (compraExitosa) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    compraExitosa = false
                    onPurchaseComplete(true)
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                    }
                }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Compra realizada") },
            text = { Text("Tu compra ha sido procesada con éxito.") }
        )
    }

    mensajeError?.let { error ->
        AlertDialog(
            onDismissRequest = { mensajeError = null },
            confirmButton = {
                TextButton(onClick = { mensajeError = null }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error en la compra") },
            text = { Text(error) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            item {
                Text(
                    "Resumen del Pedido",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        carrito.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.cantidad}x ${item.producto.nombre}", color = Color.LightGray)
                                Text("$${formatPrice(item.producto.precio * item.cantidad)}", color = Color.LightGray)
                            }
                        }
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.DarkGray
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = Color.White)
                            Text("$${formatPrice(total)}", color = Color.White)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Información de Envío",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Juan Pérez", color = Color.White)
                        Text("Av. Siempre Viva 742, Springfield", color = Color.LightGray)
                        Text("Región Metropolitana, Chile", color = Color.LightGray)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Método de Envío",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        opcionesEnvio.forEach { opcion ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = envioSeleccionado == opcion,
                                    onClick = { envioSeleccionado = opcion },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF720B0B),
                                        unselectedColor = Color.Gray
                                    )
                                )
                                Column {
                                    Text(opcion.first, color = Color.White)
                                    Text(
                                        "$${formatPrice(opcion.second)}",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total Final:",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        "$${formatPrice(totalConEnvio)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF00E676)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Button(
                    onClick = {
                        if (envioSeleccionado.first == "Estándar (3-5 días)") {
                            loading = true
                            carritoViewModel.confirmarCompra(
                                onSuccess = {
                                    loading = false
                                    compraExitosa = true
                                },
                                onError = { error ->
                                    loading = false
                                    mensajeError = error
                                    onPurchaseComplete(false)
                                }
                            )
                        } else {
                            mensajeError = "Error al procesar el envío express."
                            onPurchaseComplete(false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text("Pagar", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Volver al Inicio", color = Color.White)
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return NumberFormat.getNumberInstance(Locale("es", "CL")).format(price)
}
