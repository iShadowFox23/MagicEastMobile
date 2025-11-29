package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.viewmodel.MtgCardsViewModel
import android.icu.text.NumberFormat
import android.icu.util.ULocale
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoCartasScreen(
    navController: NavController,
    viewModel: MtgCardsViewModel = viewModel()
) {
    val state = viewModel.uiState
    val clpFormatter = NumberFormat.getCurrencyInstance(ULocale("es_CL"))

    LaunchedEffect(Unit) {
        if (viewModel.uiState.cards.isEmpty()) {
            viewModel.cargarPaginasDisponibles(query = "game:paper")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121212))
                        .padding(padding)
                ) {

                    // Lista de cartas
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.cards) { card ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        card.id?.let { id ->
                                            navController.navigate("singleCarta/$id")
                                        }
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    if (!card.imageUrl.isNullOrBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(card.imageUrl),
                                            contentDescription = card.name,
                                            modifier = Modifier.size(80.dp)
                                        )
                                    } else {
                                        Box(modifier = Modifier.size(80.dp))
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            card.name ?: "Sin Nombre",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            card.typeLine ?: "",
                                            color = Color.LightGray,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            "Valor: ${clpFormatter.format(card.valor)}",
                                            color = Color(0xFF00E676),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    //
                    PaginationSection(
                        totalPages = state.pages,
                        currentPage = state.currentPage,
                        onPageSelected = { page ->
                            viewModel.cargarPagina(page)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PaginationSection(
    totalPages: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    if (totalPages <= 1) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        // PRIMERA PÁGINA
        PaginationButton(
            page = 1,
            currentPage = currentPage,
            onPageSelected = onPageSelected
        )

        // "..." después del 1
        if (currentPage > 3) {
            Text("...", color = Color.White, modifier = Modifier.padding(horizontal = 8.dp))
        }

        // Ventana sliding: páginas cercanas a la actual
        val start = max(2, currentPage - 1)
        val end = min(totalPages - 1, currentPage + 1)

        for (page in start..end) {
            PaginationButton(
                page = page,
                currentPage = currentPage,
                onPageSelected = onPageSelected
            )
        }

        // "..." antes de la última
        if (currentPage < totalPages - 2) {
            Text("...", color = Color.White, modifier = Modifier.padding(horizontal = 8.dp))
        }

        // ÚLTIMA PÁGINA
        PaginationButton(
            page = totalPages,
            currentPage = currentPage,
            onPageSelected = onPageSelected
        )
    }
}

@Composable
fun PaginationButton(
    page: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    Button(
        onClick = { onPageSelected(page) },
        modifier = Modifier.padding(horizontal = 4.dp),
        colors = if (page == currentPage)
            ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        else
            ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
    ) {
        Text(text = page.toString())
    }
}
