
package com.example.myapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapp.data.entity.SolicitudTraslado
import com.example.myapp.data.entity.SolicitudTrasladoDao

@Database(
    entities = [SolicitudTraslado::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun solicitudTrasladoDao(): SolicitudTrasladoDao

    companion object {
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE solicitudes_traslado ADD COLUMN solicitanteUsuario TEXT NOT NULL DEFAULT ''")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerBaseDatos(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "belmonte_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
