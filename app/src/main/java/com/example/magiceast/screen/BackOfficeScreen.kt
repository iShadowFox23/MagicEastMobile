package com.example.magiceast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magiceast.model.Producto
import com.example.magiceast.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(
    onBack: () -> Unit = {},
    viewModel: AdminViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.cargarProductos(context) }

    val productos = viewModel.productos
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogAgregar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { mostrarDialogAgregar = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos disponibles", color = Color.White)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(productos, key = { it.id }) { producto ->
                        ProductoAdminCard(
                            producto = producto,
                            onEliminar = { viewModel.eliminarProducto(producto.id) },
                            onEditar = { productoAEditar = producto }
                        )
                    }
                }
            }
        }

        productoAEditar?.let { producto ->
            EditarProductoDialog(
                producto = producto,
                onDismiss = { productoAEditar = null },
                onGuardar = { nuevoNombre, nuevoPrecio ->
                    viewModel.editarProducto(producto.id, nuevoNombre, nuevoPrecio)
                    productoAEditar = null
                }
            )
        }

        if (mostrarDialogAgregar) {
            AgregarProductoDialog(
                productosExistentes = productos,
                onDismiss = { mostrarDialogAgregar = false },
                onAgregar = { nuevoProducto ->
                    viewModel.agregarProducto(nuevoProducto)
                    mostrarDialogAgregar = false
                }
            )
        }
    }
}

@Composable
fun ProductoAdminCard(
    producto: Producto,
    onEliminar: () -> Unit,
    onEditar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(producto.nombre, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text("Precio: $${producto.precio}", color = Color.LightGray)
                Text("Stock: ${producto.stock}", color = Color.Gray)
                Text(
                    "Estado: ${producto.estado}",
                    color = when (producto.estado) {
                        "Agotado" -> Color.Red
                        "Bajo Stock" -> Color(0xFFFFC107)
                        else -> Color(0xFF00E676)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.width(96.dp)
            ) {
                IconButton(
                    onClick = onEditar,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x33FFFFFF), shape = MaterialTheme.shapes.small)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFFC107))
                }

                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x33FFFFFF), shape = MaterialTheme.shapes.small)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun EditarProductoDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onGuardar: (String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue(producto.nombre)) }
    var precio by remember { mutableStateOf(TextFieldValue(producto.precio.toString())) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val nuevoPrecio = precio.text.toIntOrNull() ?: producto.precio
                onGuardar(nombre.text, nuevoPrecio)
            }) {
                Text("Guardar", color = Color(0xFF00E676))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) } },
        title = { Text("Editar Producto", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}

@Composable
fun AgregarProductoDialog(
    productosExistentes: List<Producto>,
    onDismiss: () -> Unit,
    onAgregar: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var precioAntiguo by remember { mutableStateOf(TextFieldValue("")) }
    var descuento by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var categoria by remember { mutableStateOf("Mazo Preconstruido") }
    var imagen by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var mostrarError by remember { mutableStateOf(false) }

    // Estado automático
    val estado by remember(stock.text) {
        derivedStateOf {
            val stockInt = stock.text.toIntOrNull() ?: 0
            when {
                stockInt == 0 -> "Agotado"
                stockInt in 1..4 -> "Bajo Stock"
                else -> "Disponible"
            }
        }
    }


    val precioCalculado by remember(precioAntiguo.text, descuento.text) {
        derivedStateOf {
            val precioBase = precioAntiguo.text.toIntOrNull() ?: 0
            val desc = descuento.text.toIntOrNull() ?: 0
            if (desc in 1..99) (precioBase * (100 - desc)) / 100 else precioBase
        }
    }

    val camposValidos = nombre.text.isNotBlank() &&
            precioAntiguo.text.toIntOrNull() != null &&
            stock.text.toIntOrNull() != null &&
            categoria.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (!camposValidos) {
                    mostrarError = true
                    return@TextButton
                }

                val nuevoId = (productosExistentes.maxOfOrNull { it.id } ?: 0) + 1

                val nuevoProducto = Producto(
                    id = nuevoId,
                    nombre = nombre.text.trim(),
                    precio = precioCalculado,
                    precioAntiguo = precioAntiguo.text.toIntOrNull() ?: 0,
                    descuento = descuento.text.toIntOrNull() ?: 0,
                    stock = stock.text.toIntOrNull() ?: 0,
                    categoria = categoria,
                    imagen = if (imagen.text.isNotBlank()) imagen.text else null,
                    descripcion = descripcion.text.ifBlank { null },
                    estado = estado
                )
                onAgregar(nuevoProducto)
            }) {
                Text("Agregar", color = Color(0xFF00E676))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        },
        title = { Text("Agregar Producto", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *", color = Color.Gray) },
                    isError = mostrarError && nombre.text.isBlank(),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precioAntiguo,
                    onValueChange = { if (it.text.all { ch -> ch.isDigit() }) precioAntiguo = it },
                    label = { Text("Precio Antiguo *", color = Color.Gray) },
                    isError = mostrarError && precioAntiguo.text.toIntOrNull() == null,
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descuento,
                    onValueChange = { if (it.text.all { ch -> ch.isDigit() }) descuento = it },
                    label = { Text("Descuento (%) (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Precio final: $$precioCalculado",
                    color = Color(0xFF00E676),
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { if (it.text.all { ch -> ch.isDigit() }) stock = it },
                    label = { Text("Stock *", color = Color.Gray) },
                    isError = mostrarError && stock.text.toIntOrNull() == null,
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Categoría *", color = Color.Gray)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Mazo Preconstruido", "Booster Packs").forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = { categoria = opcion },
                            label = { Text(opcion) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF2C2C2C),
                                selectedContainerColor = Color(0xFF00E676),
                                labelColor = Color.White,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("URL Imagen (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Estado automático: $estado",
                    color = when (estado) {
                        "Agotado" -> Color.Red
                        "Bajo Stock" -> Color(0xFFFFC107)
                        else -> Color(0xFF00E676)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                if (mostrarError && !camposValidos) {
                    Text(
                        "Por favor completa todos los campos obligatorios.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}
