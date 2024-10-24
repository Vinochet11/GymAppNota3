package com.example.jose_leon.Nueva_aplicacion.fragments

import android.app.AlertDialog
import android.os.Bundle
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
import com.example.jose_leon.R
import com.google.firebase.firestore.FirebaseFirestore

class CalentamientoFragment : Fragment() {

    private lateinit var btnCrearRutina: Button
    private lateinit var recyclerRutinas: RecyclerView
    private lateinit var tvNoRutina: View
    private val rutinaSeleccionada = mutableListOf<Calentamiento>()
    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection("calentamientos")

    // Definimos las listas como variables de clase
    private val calentamientosSuperior = listOf(
        Calentamiento("Rotación de hombros", "hombroo"),
        Calentamiento("Círculos de muñeca", "munecaa")
    )

    private val calentamientosInferior = listOf(
        Calentamiento("Elevación de talones", "taloness"),
        Calentamiento("Estiramiento de cuádriceps", "cuadricepp")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calentamiento, container, false)

        btnCrearRutina = view.findViewById(R.id.btn_crear_rutina)
        recyclerRutinas = view.findViewById(R.id.recycler_rutinas_creadas)
        tvNoRutina = view.findViewById(R.id.tv_no_rutina)

        recyclerRutinas.layoutManager = LinearLayoutManager(requireContext())

        btnCrearRutina.setOnClickListener {
            mostrarDialogoCrearRutina()
        }

        cargarRutinasDesdeFirestore()

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
                    guardarCalentamientoEnFirestore(calentamiento)
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

    private fun guardarCalentamientoEnFirestore(calentamiento: Calentamiento) {
        collectionRef.add(calentamiento)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Calentamiento guardado", Toast.LENGTH_SHORT)
                    .show()
                cargarRutinasDesdeFirestore()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarCalentamientoDeFirestore(calentamiento: Calentamiento) {
        collectionRef.whereEqualTo("nombre", calentamiento.nombre)
            .whereEqualTo("imagenResourceName", calentamiento.imagenResourceName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
                Toast.makeText(requireContext(), "Calentamiento eliminado", Toast.LENGTH_SHORT)
                    .show()
                cargarRutinasDesdeFirestore()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarRutinasDesdeFirestore() {
        collectionRef.get()
            .addOnSuccessListener { documents ->
                rutinaSeleccionada.clear()
                for (document in documents) {
                    val calentamiento = document.toObject(Calentamiento::class.java)
                    rutinaSeleccionada.add(calentamiento)
                }
                mostrarRutinaCreada()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarRutinaCreada() {
        if (rutinaSeleccionada.isEmpty()) {
            tvNoRutina.visibility = View.VISIBLE
            recyclerRutinas.visibility = View.GONE
        } else {
            tvNoRutina.visibility = View.GONE
            recyclerRutinas.visibility = View.VISIBLE
            recyclerRutinas.adapter = CalentamientoAdapter(rutinaSeleccionada) { calentamiento ->
                eliminarCalentamientoDeFirestore(calentamiento)
            }
        }
    }
}
