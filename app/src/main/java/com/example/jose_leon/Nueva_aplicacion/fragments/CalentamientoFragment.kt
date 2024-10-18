package com.example.jose_leon.Nueva_aplicacion.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.Nueva_aplicacion.models.CalentamientoAdapter
import com.example.jose_leon.Nueva_aplicacion.models.Calentamiento
import com.example.jose_leon.Nueva_aplicacion.adapters.RutinaCreadaAdapter
import com.example.jose_leon.R
import com.google.firebase.database.*

class CalentamientoFragment : Fragment() {

    private lateinit var btnCrearRutina: Button
    private lateinit var recyclerRutinas: RecyclerView
    private lateinit var tvNoRutina: View
    private val rutinaSeleccionada = mutableListOf<Calentamiento>()
    private lateinit var database: DatabaseReference

    // Definimos las listas como variables de clase
    private val calentamientosSuperior = listOf(
        Calentamiento("Rotación de hombros", R.drawable.calentamiento_vector),
        Calentamiento("Círculos de muñeca", R.drawable.perfil_vector)
    )

    private val calentamientosInferior = listOf(
        Calentamiento("Elevación de talones", R.drawable.recompensa_vector),
        Calentamiento("Estiramiento de cuádriceps", R.drawable.suplemento_vector)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calentamiento, container, false)

        btnCrearRutina = view.findViewById(R.id.btn_crear_rutina)
        recyclerRutinas = view.findViewById(R.id.recycler_rutinas_creadas)
        tvNoRutina = view.findViewById(R.id.tv_no_rutina)

        recyclerRutinas.layoutManager = LinearLayoutManager(requireContext())
        database = FirebaseDatabase.getInstance().getReference("calentamientos")

        btnCrearRutina.setOnClickListener {
            mostrarDialogoCrearRutina()
        }

        cargarRutinasDesdeFirebase()

        return view
    }

    private fun mostrarDialogoCrearRutina() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_crear_rutina, null)
        val spinnerPartes = dialogView.findViewById<Spinner>(R.id.spinner_partes)
        val recyclerEjercicios = dialogView.findViewById<RecyclerView>(R.id.recycler_ejercicios)

        recyclerEjercicios.layoutManager = LinearLayoutManager(requireContext())

        spinnerPartes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ejercicios =
                    if (position == 0) calentamientosSuperior else calentamientosInferior
                recyclerEjercicios.adapter = CalentamientoAdapter(ejercicios) { calentamiento ->
                    guardarCalentamientoEnFirebase(calentamiento)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Crear Rutina")
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun guardarCalentamientoEnFirebase(calentamiento: Calentamiento) {
        val ref = database.push()
        ref.setValue(calentamiento)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Calentamiento guardado", Toast.LENGTH_SHORT)
                    .show()
                cargarRutinasDesdeFirebase()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarCalentamientoDeFirebase(calentamiento: Calentamiento) {
        database.orderByChild("nombre").equalTo(calentamiento.nombre)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (calentamientoSnapshot in snapshot.children) {
                        calentamientoSnapshot.ref.removeValue()
                    }
                    Toast.makeText(requireContext(), "Calentamiento eliminado", Toast.LENGTH_SHORT)
                        .show()
                    cargarRutinasDesdeFirebase() // Actualizamos la lista
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun cargarRutinasDesdeFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rutinaSeleccionada.clear()
                for (calentamientoSnapshot in snapshot.children) {
                    val calentamiento = calentamientoSnapshot.getValue(Calentamiento::class.java)
                    calentamiento?.let { rutinaSeleccionada.add(it) }
                }
                mostrarRutinaCreada()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun mostrarRutinaCreada() {
        if (rutinaSeleccionada.isEmpty()) {
            tvNoRutina.visibility = View.VISIBLE
            recyclerRutinas.visibility = View.GONE
        } else {
            tvNoRutina.visibility = View.GONE
            recyclerRutinas.visibility = View.VISIBLE
            recyclerRutinas.adapter = CalentamientoAdapter(rutinaSeleccionada) { calentamiento ->
                eliminarCalentamientoDeFirebase(calentamiento) // Correcto callback de eliminación
            }
        }
    }
}