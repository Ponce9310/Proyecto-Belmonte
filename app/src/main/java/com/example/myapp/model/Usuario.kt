package com.example.myapp.model

import androidx.room.Entity

@Entity(tableName = "Usuario")
data class Usuario (
    val mail: String,
    val pass: String
){
}