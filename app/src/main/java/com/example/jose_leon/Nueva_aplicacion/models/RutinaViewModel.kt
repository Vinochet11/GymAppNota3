package com.example.jose_leon.Nueva_aplicacion.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class RutinaViewModel : ViewModel() {
    private val _rutinasPorDia = MutableLiveData<MutableMap<String, List<Rutina>>>(mutableMapOf())

    fun guardarRutinas(dia: String, rutinas: List<Rutina>) {
        val currentData = _rutinasPorDia.value ?: mutableMapOf()
        currentData[dia] = rutinas
        _rutinasPorDia.value = currentData
    }

    fun obtenerRutinas(dia: String): LiveData<List<Rutina>> {
        return _rutinasPorDia.map { it[dia] ?: emptyList() }
    }
}
