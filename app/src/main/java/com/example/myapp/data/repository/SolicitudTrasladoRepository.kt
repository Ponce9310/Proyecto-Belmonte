
package com.example.myapp.data.repository

import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.data.entity.SolicitudTrasladoDao
import kotlinx.coroutines.flow.Flow

class SolicitudTrasladoRepository(
    private val solicitudTrasladoDao: SolicitudTrasladoDao
) {

    suspend fun insertar(solicitud: SolicitudTraslado) {
        solicitudTrasladoDao.insertar(solicitud)
    }

    fun obtenerTodas(): Flow<List<SolicitudTraslado>> {
        return solicitudTrasladoDao.obtenerTodas()
    }

    suspend fun asignarConductor(
        solicitudId: Int,
        conductor: String
    ) {
        solicitudTrasladoDao.asignarConductor(
            solicitudId,
            conductor
        )
    }

    suspend fun actualizarEstado(
        solicitudId: Int,
        nuevoEstado: String
    ) {
        solicitudTrasladoDao.actualizarEstado(
            solicitudId,
            nuevoEstado
        )
    }
}
