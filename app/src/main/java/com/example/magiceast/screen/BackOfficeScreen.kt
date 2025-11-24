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
    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    val productos by viewModel.productos.collectAsState()
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

        // DIALOGO EDITAR
        productoAEditar?.let { producto ->
            EditarProductoDialog(
                producto = producto,
                onDismiss = { productoAEditar = null },
                onGuardar = { nombre, precio, stock, categoria, descripcion, imagen ->
                    val actualizado = producto.copy(
                        nombre = nombre,
                        precio = precio,
                        stock = stock,
                        categoria = categoria,
                        descripcion = descripcion,
                        imagen = imagen,
                        estado = calcularEstado(stock)
                    )
                    viewModel.editarProducto(actualizado)
                    productoAEditar = null
                }
            )
        }

        // DIALOGO AGREGAR
        if (mostrarDialogAgregar) {
            AgregarProductoDialog(
                onDismiss = { mostrarDialogAgregar = false },
                onAgregar = { nuevo ->
                    viewModel.agregarProducto(nuevo)
                    mostrarDialogAgregar = false
                }
            )
        }
    }
}

fun calcularEstado(stock: Int): String =
    when {
        stock == 0 -> "Agotado"
        stock in 1..4 -> "Bajo Stock"
        else -> "Disponible"
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

                val estadoColor = when (producto.estado) {
                    "Agotado" -> Color.Red
                    "Bajo Stock" -> Color(0xFFFFC107)
                    else -> Color(0xFF00E676)
                }

                Text("Estado: ${producto.estado}", color = estadoColor)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFFC107))
                }
                IconButton(onClick = onEliminar) {
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
    onGuardar: (
        nombre: String,
        precio: Int,
        stock: Int,
        categoria: String,
        descripcion: String?,
        imagen: String?
    ) -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue(producto.nombre)) }
    var precio by remember { mutableStateOf(TextFieldValue(producto.precio.toString())) }
    var stock by remember { mutableStateOf(TextFieldValue(producto.stock.toString())) }
    var categoria by remember { mutableStateOf(producto.categoria) }
    var descripcion by remember { mutableStateOf(TextFieldValue(producto.descripcion ?: "")) }
    var imagen by remember { mutableStateOf(TextFieldValue(producto.imagen ?: "")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onGuardar(
                    nombre.text,
                    precio.text.toIntOrNull() ?: producto.precio,
                    stock.text.toIntOrNull() ?: producto.stock,
                    categoria,
                    descripcion.text.ifBlank { null },
                    imagen.text.ifBlank { null }
                )
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
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Text("Categoría", color = Color.Gray)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Mazo Preconstruido", "Booster Packs").forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = { categoria = opcion },
                            label = { Text(opcion) }
                        )
                    }
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("Imagen URL (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}

@Composable
fun AgregarProductoDialog(
    onDismiss: () -> Unit,
    onAgregar: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var categoria by remember { mutableStateOf("Mazo Preconstruido") }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var imagen by remember { mutableStateOf(TextFieldValue("")) }
    var error by remember { mutableStateOf(false) }

    val camposValidos =
        nombre.text.isNotBlank() &&
                precio.text.toIntOrNull() != null &&
                stock.text.toIntOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {

                if (!camposValidos) {
                    error = true
                    return@TextButton
                }

                val nuevo = Producto(
                    id = 0, // backend genera id
                    nombre = nombre.text.trim(),
                    precio = precio.text.toInt(),
                    precioAntiguo = precio.text.toInt(),
                    descuento = 0,
                    stock = stock.text.toInt(),
                    categoria = categoria,
                    descripcion = descripcion.text.ifBlank { null },
                    imagen = imagen.text.ifBlank { null },
                    estado = calcularEstado(stock.text.toInt())
                )

                onAgregar(nuevo)

            }) {
                Text("Agregar", color = Color(0xFF00E676))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) } },
        title = { Text("Agregar Producto", color = Color.White) },
        text = {

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *", color = Color.Gray) },
                    isError = error && nombre.text.isBlank(),
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio *", color = Color.Gray) },
                    isError = error && precio.text.toIntOrNull() == null,
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock *", color = Color.Gray) },
                    isError = error && stock.text.toIntOrNull() == null,
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Text("Categoría", color = Color.Gray)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Mazo Preconstruido", "Booster Packs").forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = { categoria = opcion },
                            label = { Text(opcion) }
                        )
                    }
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("Imagen URL (opcional)", color = Color.Gray) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                if (error && !camposValidos) {
                    Text("Completa los campos obligatorios", color = Color.Red)
                }
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}
