
package com.example.myapp.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudTrasladoDao {

    @Insert
    suspend fun insertar(solicitud: SolicitudTraslado)

    @Query("SELECT * FROM solicitudes_traslado ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<SolicitudTraslado>>

    @Query("""
        UPDATE solicitudes_traslado
        SET conductorAsignado = :conductor,
            estado = 'Asignado'
        WHERE id = :solicitudId AND estado = 'Pendiente'
    """)
    suspend fun asignarConductor(
        solicitudId: Int,
        conductor: String
    )

    @Query("""
        UPDATE solicitudes_traslado
        SET estado = :nuevoEstado
        WHERE id = :solicitudId AND ((:nuevoEstado = 'En Curso' AND estado = 'Asignado') OR (:nuevoEstado = 'Finalizado' AND estado = 'En Curso'))
    """)
    suspend fun actualizarEstado(
        solicitudId: Int,
        nuevoEstado: String
    )
}
