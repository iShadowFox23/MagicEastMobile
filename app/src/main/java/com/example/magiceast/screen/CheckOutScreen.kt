
package com.example.magiceast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.magiceast.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons

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
            // Resumen del Pedido
            item {
                Text("Resumen del Pedido", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
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
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("$${formatPrice(total)}", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Información de Envío
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Información de Envío", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
                // Aquí iría un formulario, pero por ahora usamos un texto placeholder
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

            // Metodo de Envío
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Método de Envío", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 8.dp))
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
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF720B0B), unselectedColor = Color.Gray)
                                )
                                Column {
                                    Text(opcion.first, color = Color.White)
                                    Text("$${formatPrice(opcion.second)}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            // Total Final
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal=16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Final:", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("$${formatPrice(totalConEnvio)}", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }


            // Botón de Pago
            item {
                Button(
                    onClick = {
                        val success = Math.random() > 0.3 // 70% de éxito
                        onPurchaseComplete(success)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Pagar", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return NumberFormat.getNumberInstance(Locale("es", "CL")).format(price)
}