package com.example.myapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var mensajeError by mutableStateOf("")
    var nombreUsuario by mutableStateOf("")
    var rolUsuario by mutableStateOf("")

    fun cambiaMail(nuevoMail: String) { mail = nuevoMail.trim(); mensajeError = "" }
    fun cambiarPass(nuevaPass: String) { pass = nuevaPass; mensajeError = "" }

    // Cuentas locales de demostración. En producción se deben validar en un servidor.
    fun validarLogin(): Boolean {
        val cuenta = when (mail.trim().lowercase()) {
            "pasajero" -> Triple("Pasajero", "pasajero", "Pasajero123")
            "admin" -> Triple("Administrador", "admin", "Admin2026")
            "dennis" -> Triple("Conductor", "dennis", "Dennis2026")
            "jose" -> Triple("Conductor", "jose", "Jose2026")
            else -> null
        }
        if (mail.isBlank() || pass.isBlank()) {
            mensajeError = "Ingresa el usuario y la contraseña"; return false
        }
        if (cuenta == null || pass != cuenta.third) {
            mensajeError = "Usuario o contraseña incorrectos"; return false
        }
        nombreUsuario = cuenta.second
        rolUsuario = cuenta.first
        mensajeError = ""
        return true
    }
}
