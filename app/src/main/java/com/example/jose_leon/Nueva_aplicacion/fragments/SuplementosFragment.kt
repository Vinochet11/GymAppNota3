package com.example.jose_leon.Nueva_aplicacion.fragments

import android.os.Bundle
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

class SuplementosFragment : Fragment() {

    private val suplementosList = mutableListOf<Suplemento>()
    private lateinit var adapter: HistorialSuplementosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_suplementos, container, false)

        val editNombreSuplemento: EditText = view.findViewById(R.id.edit_nombre_suplemento)
        val editCantidadSuplemento: EditText = view.findViewById(R.id.edit_cantidad_suplemento)
        val btnAgregarSuplemento: Button = view.findViewById(R.id.btn_agregar_suplemento)
        val recyclerHistorial: RecyclerView = view.findViewById(R.id.recycler_historial_suplementos)

        adapter = HistorialSuplementosAdapter(suplementosList)
        recyclerHistorial.layoutManager = LinearLayoutManager(requireContext())
        recyclerHistorial.adapter = adapter

        btnAgregarSuplemento.setOnClickListener {
            val nombre = editNombreSuplemento.text.toString()
            val cantidad = editCantidadSuplemento.text.toString().toDoubleOrNull() ?: 0.0

            if (nombre.isNotBlank()) {
                val nuevoSuplemento = Suplemento(nombre, cantidad)
                suplementosList.add(nuevoSuplemento)
                adapter.notifyDataSetChanged()

                editNombreSuplemento.text.clear()
                editCantidadSuplemento.text.clear()
            }
        }

        return view
    }
}
