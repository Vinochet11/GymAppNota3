package com.example.jose_leon.Nueva_aplicacion.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jose_leon.R
import com.example.jose_leon.Nueva_aplicacion.models.Rutina
import com.example.jose_leon.Nueva_aplicacion.models.RutinaViewModel

class DiaRutinaFragment : Fragment() {
    private lateinit var diaSemana: String
    private lateinit var rutinaViewModel: RutinaViewModel

    private lateinit var textDiaSemana: TextView
    private lateinit var textRutinaDetalles: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diaSemana = arguments?.getString("diaSemana") ?: "Día"
        rutinaViewModel = ViewModelProvider(requireActivity()).get(RutinaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dia_rutina, container, false)

        textDiaSemana = view.findViewById(R.id.text_dia_semana)
        textRutinaDetalles = view.findViewById(R.id.text_rutina_detalles)
        textDiaSemana.text = diaSemana

        rutinaViewModel.obtenerRutinas(diaSemana).observe(viewLifecycleOwner) { nuevasRutinas ->
            actualizarUI(nuevasRutinas)
        }

        return view
    }

    private fun actualizarUI(rutinas: List<Rutina>) {
        if (rutinas.isNotEmpty()) {
            val detalles = rutinas.joinToString("\n") { rutina ->
                """
                    Nombre: ${rutina.nombreRutina}
                    Series y repeticiones: ${rutina.seriesRepeticiones}
                    Peso: ${rutina.peso}
                    Realizado: ${if (rutina.realizado) "Sí" else "No"}
                """.trimIndent()
            }
            textRutinaDetalles.text = detalles
        } else {
            textRutinaDetalles.text = "No hay rutinas asignadas"
        }
    }

    fun agregarOEditarRutina() {
        val rutinasActuales = rutinaViewModel.obtenerRutinas(diaSemana).value ?: emptyList()
        val dialog = RutinaDialogFragment.newInstance(rutinasActuales.toMutableList())
        dialog.setOnRutinasSavedListener { nuevasRutinas ->
            rutinaViewModel.guardarRutinas(diaSemana, nuevasRutinas)
        }
        dialog.show(parentFragmentManager, "RutinaDialog")
    }
}
