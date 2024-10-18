package com.example.jose_leon.Nueva_aplicacion.models

data class Calentamiento(
    val nombre: String = "" ,
    val imagenResId: Int = 0
){
    constructor(): this("",0)
}
