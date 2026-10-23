
package com.example.myapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.data.repository.SolicitudTrasladoRepository
import kotlinx.coroutines.flow.Flow

class SolicitudTrasladoViewModel(
    private val repository: SolicitudTrasladoRepository
) : ViewModel() {

    val solicitudes: Flow<List<SolicitudTraslado>> =
        repository.obtenerTodas()

    suspend fun guardarSolicitud(solicitud: SolicitudTraslado) {
        repository.insertar(solicitud)
    }

    suspend fun asignarConductor(
        solicitudId: Int,
        conductor: String
    ) {
        repository.asignarConductor(
            solicitudId,
            conductor
        )
    }

    suspend fun actualizarEstado(
        solicitudId: Int,
        nuevoEstado: String
    ) {
        repository.actualizarEstado(
            solicitudId,
            nuevoEstado
        )
    }
}
