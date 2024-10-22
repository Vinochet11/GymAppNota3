package com.example.jose_leon.Nueva_aplicacion.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.database.*

class RutinaViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().getReference("rutinasPorDia")
    private val _rutinasPorDia = MutableLiveData<MutableMap<String, List<Rutina>>>(mutableMapOf())

    init {
        // Cargar las rutinas desde Firebase al iniciar
        cargarRutinasDesdeFirebase()
    }

    fun guardarRutinas(dia: String, rutinas: List<Rutina>) {
        database.child(dia).setValue(rutinas)
    }

    fun obtenerRutinas(dia: String): LiveData<List<Rutina>> {
        return _rutinasPorDia.map { it[dia] ?: emptyList() }
    }

    private fun cargarRutinasDesdeFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nuevasRutinas = mutableMapOf<String, List<Rutina>>()
                for (diaSnapshot in snapshot.children) {
                    val rutinas = diaSnapshot.children.mapNotNull {
                        it.getValue(Rutina::class.java)
                    }
                    nuevasRutinas[diaSnapshot.key ?: ""] = rutinas
                }
                _rutinasPorDia.value = nuevasRutinas
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }
}
