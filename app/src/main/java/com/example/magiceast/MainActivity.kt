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
import com.example.magiceast.screen.*
import com.example.magiceast.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val catalogoViewModel = remember { CatalogoViewModel() }
            val carritoViewModel = remember { CarritoViewModel() }
            val loginViewModel = remember { LoginViewModel() }

            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    //Pantalla principal
                    composable("main") {
                        MainScreen(
                            onGoToCatalogo = { navController.navigate("catalogo") },
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("registro") }
                        )
                    }

                    //Pantalla de Login
                    composable("login") {
                        LoginScreen(
                            onBack = { navController.popBackStack() },
                            onLoginAdmin = { navController.navigate("admin") },
                            onLoginUser = { navController.navigate("catalogo") },
                            onRegister = { navController.navigate("registro") }
                        )
                    }

                    // Catálogo de productos
                    composable("catalogo") {
                        CatalogoScreen(
                            navController = navController,
                            viewModel = catalogoViewModel
                        )
                    }

                    //Detalle del producto
                    composable("detalle/{productoId}") { backStackEntry ->
                        val id = backStackEntry.arguments
                            ?.getString("productoId")
                            ?.toIntOrNull() ?: -1

                        DetalleProductoScreen(
                            productoId = id,
                            viewModel = catalogoViewModel,
                            carritoViewModel = carritoViewModel
                        )
                    }

                    //Carrito
                    composable("carrito") {
                        CarritoScreen(
                            navController = navController,
                            carritoViewModel = carritoViewModel
                        )
                    }

                    //Registro
                    composable("registro") {
                        RegistroScreen(
                            onBack = { navController.popBackStack() },
                            onRegisterSuccess = { navController.navigate("main") }
                        )
                    }

                    //Back Office (solo admin)
                    composable("admin") {
                        BackOfficeScreen(
                            onBack = {
                                loginViewModel.clearErrors()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
