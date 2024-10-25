package com.example.jose_leon.Nueva_aplicacion.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.Nueva_aplicacion.models.Suplemento
import com.example.jose_leon.Nueva_aplicacion.adapters.HistorialSuplementosAdapter
import com.example.jose_leon.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.DocumentChange

class SuplementosFragment : Fragment() {

    private val suplementosList = mutableListOf<Suplemento>()
    private lateinit var adapter: HistorialSuplementosAdapter

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // Listener para las actualizaciones en tiempo real
    private var suplementosListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_suplementos, container, false)

        val editNombreSuplemento: EditText = view.findViewById(R.id.edit_nombre_suplemento)
        val editCantidadSuplemento: EditText = view.findViewById(R.id.edit_cantidad_suplemento)
        val btnAgregarSuplemento: Button = view.findViewById(R.id.btn_agregar_suplemento)
        val recyclerHistorial: RecyclerView = view.findViewById(R.id.recycler_historial_suplementos)

        // Inicializar el adaptador con un listener para eliminar suplementos
        adapter = HistorialSuplementosAdapter(suplementosList) { suplemento ->
            eliminarSuplemento(suplemento)
        }
        recyclerHistorial.layoutManager = LinearLayoutManager(requireContext())
        recyclerHistorial.adapter = adapter

        // Configurar el botón para agregar suplementos
        btnAgregarSuplemento.setOnClickListener {
            val nombre = editNombreSuplemento.text.toString().trim()
            val cantidadStr = editCantidadSuplemento.text.toString().trim()
            val cantidad = cantidadStr.toDoubleOrNull()

            if (nombre.isNotBlank() && cantidad != null && cantidad > 0) {
                val nuevoSuplemento = Suplemento(nombre = nombre, cantidad = cantidad)
                agregarSuplemento(nuevoSuplemento)
                editNombreSuplemento.text.clear()
                editCantidadSuplemento.text.clear()
            } else {
                // Opcional: Mostrar mensaje de error al usuario
                Log.w("SuplementosFragment", "Datos de suplemento inválidos")
            }
        }

        // Cargar suplementos desde Firestore
        cargarSuplementosDesdeFirestore()

        return view
    }

    /**
     * Agrega un nuevo suplemento a Firestore
     */
    private fun agregarSuplemento(suplemento: Suplemento) {
        db.collection("suplementos")
            .add(suplemento)
            .addOnSuccessListener { documentReference ->
                Log.d("SuplementosFragment", "Suplemento agregado con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("SuplementosFragment", "Error al agregar suplemento", e)
            }
    }

    /**
     * Carga suplementos desde Firestore y actualiza la lista en tiempo real
     */
    private fun cargarSuplementosDesdeFirestore() {
        suplementosListener = db.collection("suplementos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("SuplementosFragment", "Error al escuchar suplementos", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    for (dc in snapshot.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                val suplemento = dc.document.toObject(Suplemento::class.java)
                                suplemento.id = dc.document.id // Asignar el ID del documento
                                suplementosList.add(suplemento)
                                adapter.notifyItemInserted(suplementosList.size - 1)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                val suplemento = dc.document.toObject(Suplemento::class.java)
                                suplemento.id = dc.document.id // Asignar el ID del documento
                                val index = suplementosList.indexOfFirst { it.id == dc.document.id }
                                if (index != -1) {
                                    suplementosList[index] = suplemento
                                    adapter.notifyItemChanged(index)
                                }
                            }
                            DocumentChange.Type.REMOVED -> {
                                val id = dc.document.id
                                val index = suplementosList.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    suplementosList.removeAt(index)
                                    adapter.notifyItemRemoved(index)
                                }
                            }
                        }
                    }
                } else {
                    Log.d("SuplementosFragment", "No hay suplementos")
                }
            }
    }

    /**
     * Elimina un suplemento de Firestore
     */
    private fun eliminarSuplemento(suplemento: Suplemento) {
        if (suplemento.id.isNotEmpty()) {
            db.collection("suplementos").document(suplemento.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("SuplementosFragment", "Suplemento eliminado con ID: ${suplemento.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("SuplementosFragment", "Error al eliminar suplemento", e)
                }
        } else {
            Log.w("SuplementosFragment", "Suplemento sin ID no puede ser eliminado")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        suplementosListener?.remove()
    }
}
