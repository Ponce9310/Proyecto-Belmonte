package com.example.myapp.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.viewmodel.SolicitudTrasladoViewModel
import com.example.myapp.viewmodel.SolicitudTrasladoViewModelFactory
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ConductorScreen(
    nombreConductor: String,
    onVolverClick: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: SolicitudTrasladoViewModel = viewModel(
        factory = SolicitudTrasladoViewModelFactory(context)
    )
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val nombreBuscado = nombreConductor
    val solicitudesAsignadas = solicitudes.filter {
        it.conductorAsignado.trim().equals(nombreBuscado.trim(), ignoreCase = true)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Área del conductor") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Consulta los viajes asignados a tu nombre.", style = MaterialTheme.typography.bodyLarge)
            Text("Sesión: ${nombreBuscado.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.titleMedium)
            Text("Mis traslados", style = MaterialTheme.typography.titleLarge)

            if (solicitudesAsignadas.isEmpty()) {
                Text("No hay traslados asignados a ${nombreBuscado.trim()}.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(solicitudesAsignadas, key = { it.id }) { solicitud ->
                        SolicitudConductorCard(
                            solicitud = solicitud,
                            onIniciar = {
                                scope.launch { viewModel.actualizarEstado(solicitud.id, "En Curso") }
                            },
                            onFinalizar = {
                                scope.launch { viewModel.actualizarEstado(solicitud.id, "Finalizado") }
                            }
                        )
                    }
                }
            }
            Button(onClick = onVolverClick, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al menú")
            }
        }
    }
}

@Composable
private fun SolicitudConductorCard(
    solicitud: SolicitudTraslado,
    onIniciar: () -> Unit,
    onFinalizar: () -> Unit
) {
    val context = LocalContext.current

    fun abrirMapa(destino: String) {
        val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(destino)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No se pudo abrir el mapa.", Toast.LENGTH_SHORT).show()
        }
    }

    fun navegarAlOrigen() {
        val uri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1&destination=${Uri.encode(solicitud.origen)}&travelmode=driving"
        )
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No se pudo abrir la navegación.", Toast.LENGTH_SHORT).show()
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text("Solicitud #${solicitud.id}", style = MaterialTheme.typography.titleMedium)
            Text("Origen: ${solicitud.origen}")
            Text("Destino: ${solicitud.destino}")
            Text("Fecha: ${solicitud.fecha} · Hora: ${solicitud.hora}")
            Text("Vuelo: ${solicitud.numeroVuelo.ifBlank { "No informado" }}")
            Text("Estado: ${solicitud.estado}", color = MaterialTheme.colorScheme.primary)

            Button(onClick = { abrirMapa(solicitud.origen) }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver origen en Maps")
            }
            Button(onClick = { abrirMapa(solicitud.destino) }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver destino en Maps")
            }
            if (solicitud.estado == "Asignado") {
                Button(onClick = { navegarAlOrigen() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Navegar al origen")
                }
                Button(onClick = onIniciar, modifier = Modifier.fillMaxWidth()) {
                    Text("Iniciar traslado")
                }
            } else if (solicitud.estado == "En Curso") {
                Button(onClick = onFinalizar, modifier = Modifier.fillMaxWidth()) {
                    Text("Finalizar traslado")
                }
            } else if (solicitud.estado == "Finalizado") {
                Text("Traslado finalizado.")
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
