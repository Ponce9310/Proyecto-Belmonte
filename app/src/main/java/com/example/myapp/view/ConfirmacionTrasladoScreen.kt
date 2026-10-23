
package com.example.myapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionTrasladoScreen(
    origen: String,
    destino: String,
    fecha: String,
    hora: String,
    numeroVuelo: String,
    onConfirmar: ((Boolean) -> Unit) -> Unit,
    onVolver: () -> Unit,
    onConsultar: () -> Unit
) {
    var guardando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf(false) }
    var guardadoCorrectamente by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirmar traslado") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Revisa los datos de tu solicitud",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Origen: $origen")
                    Text("Destino: $destino")
                    Text("Fecha: $fecha")
                    Text("Hora: $hora")
                    Text(
                        if (numeroVuelo.isBlank()) {
                            "Número de vuelo: No informado"
                        } else {
                            "Número de vuelo: $numeroVuelo"
                        }
                    )
                }
            }

            if (guardando) {
                Text("Guardando solicitud…")
            }

            if (guardadoCorrectamente) {
                Text(
                    "¡Solicitud guardada correctamente!",
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = onConsultar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Consultar mis solicitudes")
                }
            }

            if (mensajeError) {
                Text(
                    "No se pudo guardar la solicitud. Inténtalo nuevamente.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    guardando = true
                    mensajeError = false

                    onConfirmar { correcto ->
                        guardando = false
                        guardadoCorrectamente = correcto
                        mensajeError = !correcto
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !guardando && !guardadoCorrectamente
            ) {
                Text(if (guardando) "Guardando…" else "Confirmar solicitud")
            }

            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                enabled = !guardando && !guardadoCorrectamente
            ) {
                Text("Volver a editar")
            }
        }
    }
}
