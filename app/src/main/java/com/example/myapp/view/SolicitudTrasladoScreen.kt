
package com.example.myapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudTrasladoScreen(
    onConfirmarClick: (
        String, String, String, String, String
    ) -> Unit,
    onVolverClick: () -> Unit
) {
    var origen by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var mostrarSelectorFecha by remember { mutableStateOf(false) }
    var hora by remember { mutableStateOf(TextFieldValue(text = "")) }
    var numeroVuelo by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Solicitar traslado")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Datos del traslado",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = origen,
                onValueChange = {
                    origen = it
                    mensajeError = ""
                },
                label = {
                    Text("Origen")
                },
                placeholder = {
                    Text("Ej. Av. Apoquindo 3000, Las Condes")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = destino,
                onValueChange = {
                    destino = it
                    mensajeError = ""
                },
                label = {
                    Text("Destino")
                },
                placeholder = {
                    Text("Ej. Aeropuerto de Santiago, terminal nacional")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                readOnly = true,
                label = {
                    Text("Fecha del traslado")
                },
                placeholder = {
                    Text("Selecciona una fecha")
                },
                supportingText = {
                    Text("Pulsa el calendario para elegir el día.")
                },
                trailingIcon = {
                    IconButton(onClick = { mostrarSelectorFecha = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Abrir calendario"
                        )
                    }
                },
                isError = fecha.isNotEmpty() &&
                    (!fechaValida(fecha) || fechaEsAnteriorAHoy(fecha)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mostrarSelectorFecha = true },
                singleLine = true
            )

            if (mostrarSelectorFecha) {
                val fechaInicialMillis = remember(fecha) {
                    try {
                        LocalDate.parse(fecha, FORMATO_FECHA)
                            .atStartOfDay(ZoneOffset.UTC)
                            .toInstant()
                            .toEpochMilli()
                    } catch (_: DateTimeParseException) {
                        LocalDate.now()
                            .atStartOfDay(ZoneOffset.UTC)
                            .toInstant()
                            .toEpochMilli()
                    }
                }
                val hoyMillis = remember {
                    LocalDate.now()
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli()
                }
                val estadoFecha = rememberDatePickerState(
                    initialSelectedDateMillis = fechaInicialMillis,
                    selectableDates = object : SelectableDates {
                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                            return utcTimeMillis >= hoyMillis
                        }

                        override fun isSelectableYear(year: Int): Boolean {
                            return year >= LocalDate.now().year
                        }
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { mostrarSelectorFecha = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                estadoFecha.selectedDateMillis?.let { millis ->
                                    fecha = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                        .format(FORMATO_FECHA)
                                    mensajeError = ""
                                }
                                mostrarSelectorFecha = false
                            },
                            enabled = estadoFecha.selectedDateMillis != null
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarSelectorFecha = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = estadoFecha)
                }
            }

            OutlinedTextField(
                value = hora,
                onValueChange = { nuevoValor ->
                    val horaFormateada = formatearHoraMientrasEscribe(nuevoValor.text)
                    hora = TextFieldValue(
                        text = horaFormateada,
                        selection = TextRange(horaFormateada.length)
                    )
                    mensajeError = ""
                },
                label = {
                    Text("Hora (HH:MM)")
                },
                placeholder = {
                    Text("HH:MM")
                },
                supportingText = {
                    Text(
                        when {
                            hora.text.length == 5 && !horaValida(hora.text) ->
                                "Hora inválida. Usa 00:00 a 23:59; no se permiten horas 24 o superiores."
                            else -> "Escribe solo números; los dos puntos : se agregan automáticamente."
                        }
                    )
                },
                isError = hora.text.length == 5 && !horaValida(hora.text),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = numeroVuelo,
                onValueChange = {
                    numeroVuelo = it
                },
                label = {
                    Text("Número de vuelo (opcional)")
                },
                placeholder = {
                    Text("Ej. AB123")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    when {
                        origen.isBlank() || destino.isBlank() -> {
                            mensajeError =
                                "Ingresa las direcciones de origen y destino."
                        }

                        origen.trim().length < 5 ||
                                destino.trim().length < 5 -> {
                            mensajeError =
                                "Ingresa direcciones más completas."
                        }

                        !fechaValida(fecha) -> {
                            mensajeError =
                                "La fecha no es válida. Revisa el día, el mes y el año (DD/MM/AAAA)."
                        }

                        fechaEsAnteriorAHoy(fecha) -> {
                            mensajeError =
                                "La fecha del traslado no puede ser anterior a hoy."
                        }

                        !horaValida(hora.text) -> {
                            mensajeError =
                                "Ingresa una hora válida en formato HH:MM."
                        }

                        else -> {
                            mensajeError = ""

                            onConfirmarClick(
                                origen.trim(),
                                destino.trim(),
                                fecha.trim(),
                                hora.text.trim(),
                                numeroVuelo.trim()
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
            }

            Button(
                onClick = onVolverClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

private fun formatearFechaMientrasEscribe(entrada: String): String {
    val digitos = entrada.filter { it.isDigit() }.take(8)
    return buildString {
        digitos.forEachIndexed { indice, caracter ->
            append(caracter)
            if ((indice == 1 || indice == 3) && indice < digitos.lastIndex) {
                append('/')
            }
        }
        // Al completar día o mes, agrega el separador para que el usuario siga escribiendo números.
        if (digitos.length == 2 || digitos.length == 4) append('/')
    }.let { resultado ->
        // Evita duplicar el separador cuando ya se agregó después del último dígito.
        resultado.replace("//", "/")
    }
}

private fun formatearHoraMientrasEscribe(entrada: String): String {
    val digitos = entrada.filter { it.isDigit() }.take(4)
    return buildString {
        digitos.forEachIndexed { indice, caracter ->
            append(caracter)
            if (indice == 1 && digitos.length > 2) append(':')
        }
        if (digitos.length == 2) append(':')
    }.replace("::", ":")
}

private val FORMATO_FECHA = DateTimeFormatter
    .ofPattern("dd/MM/uuuu", Locale.getDefault())
    .withResolverStyle(ResolverStyle.STRICT)

private fun fechaValida(valor: String): Boolean {
    return try {
        LocalDate.parse(valor, FORMATO_FECHA)
        true
    } catch (_: DateTimeParseException) {
        false
    }
}

private fun fechaEsAnteriorAHoy(valor: String): Boolean {
    return try {
        LocalDate.parse(valor, FORMATO_FECHA).isBefore(LocalDate.now())
    } catch (_: DateTimeParseException) {
        true
    }
}

private fun horaValida(valor: String): Boolean {
    return valor.matches(
        Regex("^([01][0-9]|2[0-3]):[0-5][0-9]$")
    )
}
