package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.viewmodel.CatalogoViewModel
import com.example.magiceast.viewmodel.CarritoViewModel
import com.example.magiceast.viewmodel.DetalleProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    detalleViewModel: DetalleProductoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val producto = remember { viewModel.buscarProductoPorId(productoId) }
    val snackbarHostState = remember { SnackbarHostState() }

    val cantidad by detalleViewModel.cantidad
    val mensajeSnack by detalleViewModel.mensajeSnack


    LaunchedEffect(mensajeSnack) {
        mensajeSnack?.let {
            snackbarHostState.showSnackbar(it)
            detalleViewModel.resetMensaje()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF121212)
    ) { padding ->

        producto?.let { p ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen
                    Image(
                        painter = rememberAsyncImagePainter(p.imagen),
                        contentDescription = p.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Text(p.nombre, style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text(
                        "$${p.precio}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF00E676),
                        fontWeight = FontWeight.SemiBold
                    )

                    //Stock
                    if (p.stock > 0) {
                        Text("Stock disponible: ${p.stock}", color = Color.LightGray)
                    } else {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFB71C1C), shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("AGOTADO", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(p.descripcion ?: "", color = Color.LightGray)

                    Spacer(Modifier.height(12.dp))

                    //Selector de cantidad
                    if (p.stock > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { detalleViewModel.disminuirCantidad() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                            ) {
                                Text("−", color = Color.White)
                            }

                            Text("$cantidad", color = Color.White, fontWeight = FontWeight.Bold)

                            Button(
                                onClick = { detalleViewModel.aumentarCantidad(p.stock) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
                            ) {
                                Text("+", color = Color.White)
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                repeat(cantidad) { carritoViewModel.agregarProducto(p) }
                                detalleViewModel.setMensaje("${cantidad} × ${p.nombre} agregado(s)")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Agregar al carrito", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {},
                            enabled = false,
                            colors = ButtonDefaults.buttonColors(disabledContainerColor = Color.DarkGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Producto agotado", color = Color.LightGray)
                        }
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado", color = Color.White)
        }
    }
}
