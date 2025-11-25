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
                            onGoToCatalogo = { navController.navigate("catalogoProductos") },
                            onLoginClick = { navController.navigate("login") },
                            onRegisterClick = { navController.navigate("registro") },
                            onCardClick = { navController.navigate("catalogoCartas") }
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
                            carritoViewModel = carritoViewModel,
                            onFinalizarCompra = { navController.navigate("checkout") }
                        )
                    }

                    //Finalizar Compra
                    composable("checkout") {
                        CheckoutScreen(
                            navController = navController,
                            carritoViewModel = carritoViewModel,
                            onPurchaseComplete = { success ->
                                if (success) {
                                    carritoViewModel.limpiarCarrito()
                                    navController.navigate("checkoutexito")
                                } else {
                                    navController.navigate("checkoutfail")
                                }
                            }
                        )
                    }
                    //Compra exitosa
                    composable("checkoutexito") {
                        CompraExitosaScreen(
                            onContinueShopping = { navController.navigate("catalogo") },
                            onGoHome = { navController.navigate("main") }
                        )
                    }
                    //Compra fallida
                    composable("checkoutfail") {
                        CompraRechazadaScreen(
                            onRetry = {navController.navigate("checkout")},
                            onGoToCart = { navController.navigate("carrito") }
                        )
                    }
                    //Registro
                    composable("registro") {
                        RegistroScreen(
                            onBack = { navController.popBackStack() },
                            onRegisterSuccess = { navController.navigate("main") }
                        )
                    }

                    //Back Office (main)
                    composable("admin") {
                        MainBackOfficeScreen(
                            onGoToCatalogoAdmin = { navController.navigate("admincatalogo") },
                            onGoToUsuariosAdmin = { navController.navigate("adminusuarios") }
                        )
                    }

                    // NUEVA SCREEN → Gestión de Usuarios
                    composable("adminusuarios") {
                        GestionUsuariosScreen()
                    }

                    //Back Office (catalogo)
                    composable("admincatalogo") {
                        BackOfficeScreen(
                            onBack = {
                                loginViewModel.clearErrors()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }

                    //Catalogo Cartas
                    composable("catalogoCartas") {
                        CatalogoCartasScreen(navController = navController)
                    }

                    // Single carta
                    composable("singleCarta/{cartaId}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("cartaId") ?: ""
                        SingleCartaScreen(
                            navController = navController,
                            cartaId = id,
                            carritoViewModel = carritoViewModel
                        )
                    }
                    //Catalogo Productos
                    composable("catalogoProductos") {
                        CatalogoProductosScreen(navController = navController)
                    }
                }
            }
        }
    }
}
