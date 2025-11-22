package com.example.magiceast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.magiceast.viewmodel.MtgCardsViewModel

@Composable
fun MtgCardsScreen(
    viewModel: MtgCardsViewModel = viewModel()
) {
    val state = viewModel.uiState

    // Cargar cartas al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarCards(pageSize = 30)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        when {
            state.loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF720B0B))
                }
            }

            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ocurrió un error:",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        color = Color.Red
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.cargarCards(pageSize = 30) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF720B0B))
                    ) {
                        Text("Reintentar", color = Color.White)
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.cards) { card ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .heightIn(min = 80.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // Imagen de la carta (si tiene)
                                if (!card.imageUrl.isNullOrBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(card.imageUrl),
                                        contentDescription = card.name,
                                        modifier = Modifier
                                            .size(64.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp),
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = card.name ?: "Sin nombre",
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = card.type ?: "",
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    if (!card.manaCost.isNullOrBlank()) {
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            text = "Coste: ${card.manaCost}",
                                            color = Color(0xFFCCCCCC),
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