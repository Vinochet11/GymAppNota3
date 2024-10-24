package com.example.jose_leon.Nueva_aplicacion.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rutina(
    val nombreRutina: String,
    val seriesRepeticiones: String,
    val peso: String,
    val realizado: Boolean = false,
    val imagenUrl: String? = null  // Nueva propiedad
) : Parcelable
 {

    constructor() : this("", "", "", false)
}
