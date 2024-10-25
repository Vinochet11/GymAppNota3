package com.example.jose_leon.Nueva_aplicacion.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jose_leon.Nueva_aplicacion.models.UserProfile
import com.example.jose_leon.R
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {

    private lateinit var editPesoActual: EditText
    private lateinit var editPesoMeta: EditText
    private lateinit var editMasaMuscularActual: EditText
    private lateinit var editMasaMuscularMeta: EditText
    private lateinit var btnGuardarPerfil: Button


    private val db = FirebaseFirestore.getInstance()


    private val perfilDocId = "perfil_usuario"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializar vistas
        editPesoActual = view.findViewById(R.id.editPesoActual)
        editPesoMeta = view.findViewById(R.id.editPesoMeta)
        editMasaMuscularActual = view.findViewById(R.id.editMasaMuscularActual)
        editMasaMuscularMeta = view.findViewById(R.id.editMasaMuscularMeta)
        btnGuardarPerfil = view.findViewById(R.id.btnGuardarPerfil)

        // Cargar datos existentes del perfil
        cargarPerfil()

        // Configurar el botón para guardar perfil
        btnGuardarPerfil.setOnClickListener {
            guardarPerfil()
        }

        return view
    }


    private fun cargarPerfil() {
        db.collection("perfiles")
            .document(perfilDocId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val perfil = document.toObject(UserProfile::class.java)
                    perfil?.let {
                        editPesoActual.setText(it.kilosActuales.toString())
                        editPesoMeta.setText(it.MetaKilos.toString())
                        editMasaMuscularActual.setText(it.MasaMuscular.toString())
                        editMasaMuscularMeta.setText(it.MetaMasaMuscular.toString())
                    }
                } else {
                    Log.d("PerfilFragment", "No se encontró el perfil del usuario.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PerfilFragment", "Error al obtener el perfil: ", exception)
                Toast.makeText(requireContext(), "Error al cargar perfil.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun guardarPerfil() {
        val pesoActual = editPesoActual.text.toString().toDoubleOrNull()
        val pesoMeta = editPesoMeta.text.toString().toDoubleOrNull()
        val masaMuscularActual = editMasaMuscularActual.text.toString().toDoubleOrNull()
        val masaMuscularMeta = editMasaMuscularMeta.text.toString().toDoubleOrNull()

        if (pesoActual == null || pesoMeta == null || masaMuscularActual == null || masaMuscularMeta == null) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val perfil = UserProfile(
            id = perfilDocId,
            kilosActuales  = pesoActual,
            MetaKilos = pesoMeta,
            MasaMuscular = masaMuscularActual,
            MetaMasaMuscular = masaMuscularMeta
        )

        db.collection("perfiles")
            .document(perfilDocId)
            .set(perfil)
            .addOnSuccessListener {
                Log.d("PerfilFragment", "Perfil guardado exitosamente.")
                Toast.makeText(requireContext(), "Perfil guardado.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Error al guardar el perfil: ", e)
                Toast.makeText(requireContext(), "Error al guardar el perfil.", Toast.LENGTH_SHORT).show()
            }
    }
}
