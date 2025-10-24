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
import com.example.magiceast.screen.MainScreen
import com.example.magiceast.screen.CatalogoScreen
import com.example.magiceast.screen.DetalleProductoScreen
import com.example.magiceast.screen.LoginScreen

import com.example.magiceast.viewmodel.CatalogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val catalogoViewModel = remember { CatalogoViewModel() }

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = navController, startDestination = "main") {

                    // Pantalla principal
                    composable("main") {
                        MainScreen(
                            onGoToCatalogo = { navController.navigate("catalogo") },
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("registro") }
                        )
                    }

                    // Pantalla de Login
                    composable("login") {
                        LoginScreen(
                            onBack = { navController.popBackStack() },
                            onLoginSuccess = { navController.navigate("catalogo") },
                            onRegister = { navController.navigate("registro") }
                        )
                    }


                    // Catálogo
                    composable("catalogo") {
                        CatalogoScreen(navController, catalogoViewModel)
                    }

                    // Detalle de producto
                    composable("detalle/{productoId}") { backStack ->
                        val id = backStack.arguments?.getString("productoId")?.toIntOrNull() ?: -1
                        DetalleProductoScreen(id, catalogoViewModel)
                    }
                }
            }
        }
    }
}
