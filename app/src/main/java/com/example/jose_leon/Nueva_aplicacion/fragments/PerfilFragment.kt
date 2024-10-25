// Archivo: PerfilFragment.kt
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.Nueva_aplicacion.adapters.RecompensasAdapter
import com.example.jose_leon.Nueva_aplicacion.models.Recompensa
import com.example.jose_leon.Nueva_aplicacion.models.UserProfile
import com.example.jose_leon.R
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {

    private lateinit var editPesoActual: EditText
    private lateinit var editPesoMeta: EditText
    private lateinit var editMasaMuscularActual: EditText
    private lateinit var editMasaMuscularMeta: EditText
    private lateinit var btnGuardarPerfil: Button
    private lateinit var recyclerRecompensas: RecyclerView

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // ID del documento de perfil (puede ser el UID del usuario si usas Firebase Auth)
    private val perfilDocId = "perfil_usuario" // Cambia esto según tu lógica de identificación de usuario

    // Lista de recompensas
    private val recompensasList = mutableListOf<Recompensa>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializar vistas
        editPesoActual = view.findViewById(R.id.editPesoActual)
        editPesoMeta = view.findViewById(R.id.editPesoMeta)
        editMasaMuscularActual = view.findViewById(R.id.editMasaMuscularActual)
        editMasaMuscularMeta = view.findViewById(R.id.editMasaMuscularMeta)
        btnGuardarPerfil = view.findViewById(R.id.btnGuardarPerfil)
        recyclerRecompensas = view.findViewById(R.id.recyclerRecompensas)

        // Configurar el RecyclerView para recompensas
        recyclerRecompensas.layoutManager = GridLayoutManager(requireContext(), 3) // 3 columnas
        recyclerRecompensas.adapter = RecompensasAdapter(recompensasList, listOf()) // Inicialmente vacío

        // Cargar recompensas desde Firestore
        cargarRecompensas()

        // Cargar datos existentes del perfil
        cargarPerfil()

        // Configurar el botón para guardar perfil
        btnGuardarPerfil.setOnClickListener {
            guardarPerfil()
        }

        return view
    }

    /**
     * Carga todas las recompensas desde Firestore
     */
    private fun cargarRecompensas() {
        db.collection("recompensas")
            .get()
            .addOnSuccessListener { result ->
                recompensasList.clear()
                for (document in result) {
                    val recompensa = document.toObject(Recompensa::class.java)
                    recompensa.id = document.id
                    recompensasList.add(recompensa)
                    Log.d("RecompensaCargada", "Recompensa: ${recompensa.nombre}, ID: ${recompensa.id}")
                }
                // Notificar al adaptador
                recyclerRecompensas.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("PerfilFragment", "Error al cargar recompensas: ", exception)
                Toast.makeText(requireContext(), "Error al cargar recompensas.", Toast.LENGTH_SHORT).show()
            }
    }


    /**
     * Carga el perfil del usuario desde Firestore
     */
    private fun cargarPerfil() {
        db.collection("usuarios")
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
                        verificarRecompensas(it)
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

    /**
     * Guarda o actualiza el perfil del usuario en Firestore
     */
    private fun guardarPerfil() {
        val kilosActuales = editPesoActual.text.toString().toDoubleOrNull()
        val MetaKilos = editPesoMeta.text.toString().toDoubleOrNull()
        val MasaMuscular = editMasaMuscularActual.text.toString().toDoubleOrNull()
        val MetaMasaMuscular = editMasaMuscularMeta.text.toString().toDoubleOrNull()

        if (kilosActuales == null || MetaKilos == null || MasaMuscular == null || MetaMasaMuscular == null) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos correctamente.", Toast.LENGTH_SHORT).show()
            return
        }

        // Es importante mantener los otros campos como 'rutinasCompletas' y 'recompensasDesbloquedas'
        val perfil = UserProfile(
            id = perfilDocId,
            kilosActuales = kilosActuales,
            MetaKilos = MetaKilos,
            MasaMuscular = MasaMuscular,
            MetaMasaMuscular = MetaMasaMuscular,
            // 'rutinasCompletas' y 'recompensasDesbloquedas' deberían mantener sus valores actuales
            // Para ello, primero necesitas obtenerlos
            rutinasCompletas = 0, // Esto debería ser actualizado correctamente
            recompensasDesbloquedas = listOf() // Esto también debería mantener sus valores actuales
        )

        db.collection("usuarios")
            .document(perfilDocId)
            .set(perfil)
            .addOnSuccessListener {
                Log.d("PerfilFragment", "Perfil guardado exitosamente.")
                Toast.makeText(requireContext(), "Perfil guardado.", Toast.LENGTH_SHORT).show()
                verificarRecompensas(perfil)
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Error al guardar el perfil: ", e)
                Toast.makeText(requireContext(), "Error al guardar el perfil.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Verifica si se han cumplido las metas para desbloquear recompensas
     */
    private fun verificarRecompensas(perfil: UserProfile) {
        val db = FirebaseFirestore.getInstance()
        val usuarioRef = db.collection("usuarios").document(perfil.id)
        val recompensasDesbloqueadas = perfil.recompensasDesbloquedas.toMutableList()

        for (recompensa in recompensasList) {
            // Verificar si la recompensa ya está desbloqueada
            if (!recompensasDesbloqueadas.contains(recompensa.id)) {
                var metaCumplida = false
                when (recompensa.metaTipo) {
                    "peso" -> {
                        if (perfil.kilosActuales <= recompensa.metaValor) { // Asumiendo que la meta es reducir peso
                            metaCumplida = true
                        }
                    }
                    "masaMuscular" -> {
                        if (perfil.MasaMuscular >= recompensa.metaValor) { // Aumentar masa muscular
                            metaCumplida = true
                        }
                    }
                    "rutinasCompletas" -> {
                        if (perfil.rutinasCompletas >= recompensa.metaValor.toInt()) {
                            metaCumplida = true
                        }
                    }
                    // Agrega más tipos de metas según tus necesidades
                }

                if (metaCumplida) {
                    recompensasDesbloqueadas.add(recompensa.id)
                    Toast.makeText(requireContext(), "¡Has desbloqueado la recompensa: ${recompensa.nombre}!", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Actualizar la lista de recompensas desbloqueadas en Firestore
        usuarioRef.update("recompensasDesbloquedas", recompensasDesbloqueadas)
            .addOnSuccessListener {
                Log.d("PerfilFragment", "Recompensas actualizadas exitosamente.")
                // Actualizar el adaptador
                (recyclerRecompensas.adapter as? RecompensasAdapter)?.let { adapter ->
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.e("PerfilFragment", "Error al actualizar recompensas: ", e)
            }
    }
}

