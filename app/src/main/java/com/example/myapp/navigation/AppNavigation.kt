
package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.view.AdministradorScreen
import com.example.myapp.view.ConductorScreen
import com.example.myapp.view.ConsultarSolicitudesScreen
import com.example.myapp.view.ConfirmacionTrasladoScreen
import com.example.myapp.view.HomeScreen
import com.example.myapp.view.LoginScreen
import com.example.myapp.view.PasajeroScreen
import com.example.myapp.view.QrConductorScreen
import com.example.myapp.view.SolicitudTrasladoScreen
import com.example.myapp.viewmodel.SolicitudTrasladoViewModel
import com.example.myapp.viewmodel.SolicitudTrasladoViewModelFactory
import kotlinx.coroutines.launch

private const val QR_CONDUCTOR = "qr_conductor"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombreUsuario by remember { mutableStateOf("") }
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var numeroVuelo by remember { mutableStateOf("") }

    val solicitudViewModel: SolicitudTrasladoViewModel = viewModel(
        factory = SolicitudTrasladoViewModelFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { nombre, rol ->
                    nombreUsuario = nombre

                    val rutaDestino = when (rol) {
                        "Administrador" -> Routes.ADMIN
                        "Conductor" -> Routes.CONDUCTOR
                        else -> Routes.PASAJERO
                    }

                    navController.navigate(rutaDestino) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.PASAJERO) {
            PasajeroScreen(
                onSolicitarClick = {
                    navController.navigate(Routes.SOLICITAR_TRASLADO)
                },
                onConsultarClick = {
                    navController.navigate(Routes.CONSULTAR_SOLICITUDES)
                },
                onCerrarSesionClick = {
                    nombreUsuario = ""
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(QR_CONDUCTOR) {
            QrConductorScreen(
                onConfirmar = {
                    navController.popBackStack()
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onPasajeroClick = {
                    navController.navigate(Routes.SOLICITAR_TRASLADO)
                },
                onAdminClick = {
                    navController.navigate(Routes.ADMIN)
                },
                onConductorClick = {
                    navController.navigate(Routes.CONDUCTOR)
                }
            )
        }

        composable(Routes.ADMIN) {
            AdministradorScreen(
                viewModel = solicitudViewModel,
                onVolverClick = {
                    nombreUsuario = ""
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.CONDUCTOR) {
            ConductorScreen(
                nombreConductor = nombreUsuario,
                onVolverClick = {
                    nombreUsuario = ""
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.SOLICITAR_TRASLADO) {
            SolicitudTrasladoScreen(
                onConfirmarClick = { o, d, f, h, vuelo ->
                    origen = o
                    destino = d
                    fecha = f
                    hora = h
                    numeroVuelo = vuelo

                    navController.navigate(Routes.CONFIRMAR_TRASLADO)
                },
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.CONFIRMAR_TRASLADO) {
            ConfirmacionTrasladoScreen(
                origen = origen,
                destino = destino,
                fecha = fecha,
                hora = hora,
                numeroVuelo = numeroVuelo,
                onConfirmar = { resultado ->
                    scope.launch {
                        try {
                            solicitudViewModel.guardarSolicitud(
                                SolicitudTraslado(
                                    origen = origen,
                                    destino = destino,
                                    fecha = fecha,
                                    hora = hora,
                                    numeroVuelo = numeroVuelo,
                                    solicitanteUsuario = nombreUsuario
                                )
                            )
                            resultado(true)
                        } catch (e: Exception) {
                            resultado(false)
                        }
                    }
                },
                onVolver = {
                    navController.popBackStack()
                },
                onConsultar = {
                    origen = ""
                    destino = ""
                    fecha = ""
                    hora = ""
                    numeroVuelo = ""

                    navController.navigate(Routes.CONSULTAR_SOLICITUDES) {
                        popUpTo(Routes.SOLICITAR_TRASLADO) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.CONSULTAR_SOLICITUDES) {
            ConsultarSolicitudesScreen(
                usuarioActual = nombreUsuario,
                onVolverClick = {
                    navController.popBackStack()
                },
                onEscanearQrClick = {
                    navController.navigate(QR_CONDUCTOR)
                }
            )
        }
    }
}
