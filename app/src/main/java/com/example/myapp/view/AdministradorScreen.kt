package com.example.myapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.viewmodel.SolicitudTrasladoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministradorScreen(
    viewModel: SolicitudTrasladoViewModel,
    onVolverClick: () -> Unit
) {
    val solicitudes by viewModel.solicitudes.collectAsState(initial = emptyList())
    val conductores = remember { mutableStateMapOf<Int, String>() }
    val coroutineScope = rememberCoroutineScope()
    val conductoresDisponibles = listOf("Dennis", "Jose")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Administración de traslados") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Solicitudes registradas", style = MaterialTheme.typography.titleLarge)
            Text(
                "Selecciona un conductor de la lista. No se permite asignarlo si ya tiene otro traslado registrado para la misma fecha y hora.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (solicitudes.isEmpty()) {
                Text("Todavía no hay solicitudes registradas.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = solicitudes, key = { it.id }) { solicitud ->
                        val conductorSeleccionado = conductores[solicitud.id].orEmpty()
                        val conflicto = conductorSeleccionado.isNotBlank() && solicitudes.any { otra ->
                            otra.id != solicitud.id &&
                                otra.conductorAsignado.equals(conductorSeleccionado, ignoreCase = true) &&
                                otra.fecha.trim() == solicitud.fecha.trim() &&
                                otra.hora.trim() == solicitud.hora.trim() &&
                                otra.estado in setOf("Asignado", "En Curso")
                        }
                        TarjetaSolicitudAdmin(
                            solicitud = solicitud,
                            conductor = conductorSeleccionado,
                            conductoresDisponibles = conductoresDisponibles,
                            hayConflictoHorario = conflicto,
                            onConductorChange = { conductores[solicitud.id] = it },
                            onAsignarClick = {
                                val nombre = conductores[solicitud.id].orEmpty()
                                if (nombre in conductoresDisponibles && !conflicto && solicitud.estado == "Pendiente") {
                                    coroutineScope.launch {
                                        viewModel.asignarConductor(solicitud.id, nombre.lowercase())
                                    }
                                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TarjetaSolicitudAdmin(
    solicitud: SolicitudTraslado,
    conductor: String,
    conductoresDisponibles: List<String>,
    hayConflictoHorario: Boolean,
    onConductorChange: (String) -> Unit,
    onAsignarClick: () -> Unit
) {
    val sePuedeAsignar = solicitud.estado == "Pendiente"
    var menuExpandido by remember(solicitud.id) { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Solicitud #${solicitud.id}", style = MaterialTheme.typography.titleMedium)
            Text("Origen: ${solicitud.origen}")
            Text("Destino: ${solicitud.destino}")
            Text("Fecha: ${solicitud.fecha}")
            Text("Hora: ${solicitud.hora}")
            Text("Estado: ${solicitud.estado}")

            if (solicitud.conductorAsignado.isNotBlank()) {
                Text("Conductor asignado: ${solicitud.conductorAsignado.replaceFirstChar { it.uppercase() }}")
            }

            when (solicitud.estado) {
                "Asignado" -> Text("Asignación confirmada. Para cambiarla, debe gestionarse como una incidencia administrativa.")
                "En Curso" -> Text("Viaje en curso: el conductor queda bloqueado.")
                "Finalizado" -> Text("Viaje finalizado. No se puede modificar la asignación.")
                else -> {
                    ExposedDropdownMenuBox(
                        expanded = menuExpandido,
                        onExpandedChange = { menuExpandido = it }
                    ) {
                        OutlinedTextField(
                            value = conductor,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Conductor registrado") },
                            placeholder = { Text("Seleccionar conductor") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = menuExpandido,
                            onDismissRequest = { menuExpandido = false }
                        ) {
                            conductoresDisponibles.forEach { nombre ->
                                DropdownMenuItem(
                                    text = { Text(nombre) },
                                    onClick = {
                                        onConductorChange(nombre)
                                        menuExpandido = false
                                    }
                                )
                            }
                        }
                    }
                    if (hayConflictoHorario) {
                        Text(
                            "Conflicto de horario: este conductor ya tiene un traslado asignado o en curso en esta misma fecha y hora.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = onAsignarClick,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = conductor in conductoresDisponibles && !hayConflictoHorario
                    ) {
                        Text("Confirmar asignación")
                    }
                }
            }
        }
    }
}
