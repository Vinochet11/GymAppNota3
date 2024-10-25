package com.example.jose_leon.Nueva_aplicacion.models

import android.util.Log
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

    /**
     * Guarda una lista de rutinas para un día específico.
     * Utiliza la clase RutinasPorDia para facilitar la serialización.
     */
    fun guardarRutinas(dia: String, rutinas: List<Rutina>) {
        val rutinasPorDia = RutinasporDia(rutinas)
        Log.d("RutinaViewModel", "Guardando rutinas para el día: $dia")
        rutinas.forEach { rutina ->
            Log.d("RutinaViewModel", "Rutina a guardar: $rutina")
        }
        db.collection("rutinasPorDia").document(dia).set(rutinasPorDia)
            .addOnSuccessListener {
                Log.d("RutinaViewModel", "Rutinas guardadas exitosamente para el día $dia")
            }
            .addOnFailureListener { e ->
                Log.e("RutinaViewModel", "Error al guardar rutinas para el día $dia", e)
            }
    }

    /**
     * Obtiene las rutinas para un día específico.
     * Retorna un LiveData que observa los cambios en Firestore.
     */
    fun obtenerRutinas(dia: String): LiveData<List<Rutina>> {
        return _rutinasPorDia.map { it[dia] ?: emptyList() }
    }

    /**
     * Carga las rutinas desde Firestore y actualiza el LiveData.
     */
    private fun cargarRutinasDesdeFirestore() {
        db.collection("rutinasPorDia").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("RutinaViewModel", "Error al cargar rutinas", e)
                return@addSnapshotListener
            }
            val nuevasRutinas = mutableMapOf<String, List<Rutina>>()
            snapshot?.documents?.forEach { doc ->
                val rutinasPorDia = doc.toObject(RutinasporDia::class.java)
                Log.d("RutinaViewModel", "Documento: ${doc.id}, RutinasPorDia: $rutinasPorDia")
                rutinasPorDia?.rutinas?.let {
                    nuevasRutinas[doc.id] = it
                    it.forEach { rutina ->
                        Log.d("RutinaViewModel", "Rutina cargada: $rutina")
                    }
                }
            }
            _rutinasPorDia.value = nuevasRutinas
        }
    }
}