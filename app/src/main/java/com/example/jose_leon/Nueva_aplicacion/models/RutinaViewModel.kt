package com.example.jose_leon.Nueva_aplicacion.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.firestore.FirebaseFirestore

class RutinaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _rutinasPorDia = MutableLiveData<Map<String, List<Rutina>>>()

    init {
        cargarRutinasDesdeFirestore()
    }

    fun guardarRutinas(dia: String, rutinas: List<Rutina>) {
        db.collection("rutinasPorDia").document(dia).set(mapOf("rutinas" to rutinas))
    }

    fun obtenerRutinas(dia: String): LiveData<List<Rutina>> {
        return _rutinasPorDia.map { it[dia] ?: emptyList() }
    }

    private fun cargarRutinasDesdeFirestore() {
        db.collection("rutinasPorDia").addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Manejar error si ocurre
                return@addSnapshotListener
            }
            val nuevasRutinas = mutableMapOf<String, List<Rutina>>()
            snapshot?.documents?.forEach { doc ->
                val rutinas = doc["rutinas"] as? List<Rutina> ?: emptyList()
                nuevasRutinas[doc.id] = rutinas
            }
            _rutinasPorDia.value = nuevasRutinas
        }
    }
}

