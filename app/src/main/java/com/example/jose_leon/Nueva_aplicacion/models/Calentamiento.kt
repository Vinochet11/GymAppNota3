package com.example.jose_leon.Nueva_aplicacion.models

data class Calentamiento(
    val nombre: String = "",
    val imagenResourceName: String = ""
) {
    constructor() : this("", "")
}
