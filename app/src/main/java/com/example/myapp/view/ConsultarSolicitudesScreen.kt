
package com.example.myapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.viewmodel.SolicitudTrasladoViewModel
import com.example.myapp.viewmodel.SolicitudTrasladoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultarSolicitudesScreen(
    usuarioActual: String,
    onVolverClick: () -> Unit,
    onEscanearQrClick: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: SolicitudTrasladoViewModel = viewModel(
        factory = SolicitudTrasladoViewModelFactory(context)
    )

    val todasLasSolicitudes by viewModel.solicitudes.collectAsState(
        initial = emptyList()
    )

    val solicitudes = todasLasSolicitudes.filter {
        it.solicitanteUsuario == usuarioActual
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis solicitudes")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Button(
                onClick = onVolverClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Estado de tus traslados",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (solicitudes.isEmpty()) {
                Text("Todavía no tienes solicitudes registradas.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = solicitudes,
                        key = { it.id }
                    ) { solicitud ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Solicitud #${solicitud.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text("Origen: ${solicitud.origen}")
                                Text("Destino: ${solicitud.destino}")
                                Text("Fecha: ${solicitud.fecha}")
                                Text("Hora: ${solicitud.hora}")

                                Text(
                                    text = if (solicitud.numeroVuelo.isBlank()) {
                                        "Número de vuelo: No informado"
                                    } else {
                                        "Número de vuelo: ${solicitud.numeroVuelo}"
                                    }
                                )

                                Text(
                                    text = "Estado: ${solicitud.estado}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = if (
                                        solicitud.conductorAsignado.isBlank()
                                    ) {
                                        "Conductor asignado: Pendiente de asignación"
                                    } else {
                                        "Conductor asignado: ${
                                            solicitud.conductorAsignado
                                                .replaceFirstChar { it.uppercase() }
                                        }"
                                    },
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (solicitud.conductorAsignado.isNotBlank()) {
                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )

                                    Button(
                                        onClick = onEscanearQrClick,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Escanear QR de Belmonte")
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
