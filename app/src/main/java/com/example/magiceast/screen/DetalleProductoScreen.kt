package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(productoId: Int, viewModel: CatalogoViewModel) {
    val producto = remember { viewModel.buscarProductoPorId(productoId) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(producto?.nombre ?: "Detalle") })
    }) { padding ->
        producto?.let { p ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(p.imagen),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                Text("$${p.precio}", style = MaterialTheme.typography.titleMedium)
                Text(p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
                Button(
                    onClick = { /* TODO: agregar al carrito */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado")
        }
    }
}