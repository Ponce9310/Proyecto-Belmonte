
package com.example.myapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes_traslado")
data class SolicitudTraslado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val origen: String,
    val destino: String,
    val fecha: String,
    val hora: String,
    val numeroVuelo: String,

    val estado: String = "Pendiente",
    val conductorAsignado: String = "",
    val solicitanteUsuario: String = ""
)
