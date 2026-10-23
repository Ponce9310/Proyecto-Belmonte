
package com.example.myapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.data.database.AppDatabase
import com.example.myapp.data.repository.SolicitudTrasladoRepository

class SolicitudTrasladoViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(
                SolicitudTrasladoViewModel::class.java
            )
        ) {
            val database = AppDatabase.obtenerBaseDatos(context)
            val repository = SolicitudTrasladoRepository(
                database.solicitudTrasladoDao()
            )

            @Suppress("UNCHECKED_CAST")
            return SolicitudTrasladoViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
