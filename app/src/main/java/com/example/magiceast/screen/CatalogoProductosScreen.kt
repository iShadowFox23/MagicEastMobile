package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.magiceast.viewmodel.ProductoApiViewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoProductosScreen(
    navController: NavController,
    viewModel: ProductoApiViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo (API MagicEast)") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = false }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Ir al carrito",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        when {
            state.loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121212))
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121212))
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${state.error}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121212))
                        .padding(padding),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.productos) { producto ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                        navController.navigate("detalle/${producto.id}")

                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Imagen del producto desde assets (si viene ruta)
                                if (!producto.imagen.isNullOrBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(producto.imagen),
                                        contentDescription = producto.nombre,
                                        modifier = Modifier
                                            .size(80.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    // Placeholder si no hay imagen
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .background(Color.DarkGray)
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        producto.nombre,
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Precio: ${producto.precio} CLP",
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Stock: ${producto.stock}",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    producto.descripcion?.let {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            it,
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
