package com.example.magiceast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.magiceast.screen.CatalogoScreen
import com.example.magiceast.screen.DetalleProductoScreen
import com.example.magiceast.viewmodel.CatalogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = remember { CatalogoViewModel() }

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = navController, startDestination = "catalogo") {
                    composable("catalogo") {
                        CatalogoScreen(navController, viewModel)
                    }
                    composable("detalle/{productoId}") { backStack ->
                        val id = backStack.arguments?.getString("productoId")?.toIntOrNull() ?: -1
                        DetalleProductoScreen(id, viewModel)
                    }
                }
            }
        }
    }
}