package com.example.magiceast.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.model.Producto
import com.example.magiceast.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(
    onBack: () -> Unit = {},
    viewModel: AdminViewModel = viewModel()
) {
    val context = LocalContext.current

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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
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
                    Text("No hay productos", color = Color.White)
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

        //Editar
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

        //Agregar
        if (mostrarDialogAgregar) {
            AgregarProductoDialog(
                onDismiss = { mostrarDialogAgregar = false },
                onAgregar = { nuevoProducto, imagenUri ->
                    viewModel.agregarProducto(context, nuevoProducto, imagenUri)
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, color = Color.White)
                Text("Precio: ${producto.precio}", color = Color.LightGray)
                Text("Stock: ${producto.stock}", color = Color.Gray)
            }

            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Yellow)
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
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
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        title = { Text("Editar Producto", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
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
                    label = { Text("Descripción") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("Imagen (solo lectura)") },
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
    onAgregar: (Producto, Uri?) -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var precio by remember { mutableStateOf(TextFieldValue("")) }
    var stock by remember { mutableStateOf(TextFieldValue("")) }
    var categoria by remember { mutableStateOf("Mazo Preconstruido") }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var error by remember { mutableStateOf(false) }

    // Imagen
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imagenUri = uri }

    val camposValidos =
        nombre.text.isNotBlank() &&
                precio.text.toIntOrNull() != null &&
                stock.text.toIntOrNull() != null &&
                imagenUri != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Producto", color = Color.White) },
        confirmButton = {
            TextButton(onClick = {

                if (!camposValidos) {
                    error = true
                    return@TextButton
                }

                val nuevo = Producto(
                    id = 0,
                    nombre = nombre.text.trim(),
                    precio = precio.text.toInt(),
                    precioAntiguo = precio.text.toInt(),
                    descuento = 0,
                    stock = stock.text.toInt(),
                    categoria = categoria,
                    descripcion = descripcion.text.ifBlank { null },
                    imagen = null,
                    estado = calcularEstado(stock.text.toInt())
                )

                onAgregar(nuevo, imagenUri)

            }) {
                Text("Agregar", color = Color(0xFF00E676))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio *") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock *") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                Text("Categoría", color = Color.Gray)
                Row {
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
                    label = { Text("Descripción") },
                    textStyle = LocalTextStyle.current.copy(color = Color.White)
                )

                // Seleccionar imagen
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }

                imagenUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                }

                if (error && !camposValidos) {
                    Text("Completa todos los campos", color = Color.Red)
                }
            }
        },
        containerColor = Color(0xFF1E1E1E)
    )
}
