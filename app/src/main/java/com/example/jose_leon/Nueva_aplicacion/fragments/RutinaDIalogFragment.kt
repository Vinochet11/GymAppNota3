package com.example.jose_leon.Nueva_aplicacion.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.jose_leon.Nueva_aplicacion.models.Rutina
import com.example.jose_leon.R

class RutinaDialogFragment : DialogFragment() {

    private var rutinasExistentes: MutableList<Rutina>? = null
    private var onRutinasSaved: ((List<Rutina>) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rutinasExistentes = arguments?.getParcelableArrayList("rutinasExistentes")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_rutina_dialog, null)

        // Listas para almacenar
        val nombreRutinaEditTexts = mutableListOf<EditText>()
        val seriesRepeticionesEditTexts = mutableListOf<EditText>()
        val pesoEditTexts = mutableListOf<EditText>()
        val realizadoCheckBoxes = mutableListOf<CheckBox>()

        // Inicialización de los campos
        for (i in 1..5) {
            val nombreId = resources.getIdentifier("edit_nombre_rutina$i", "id", requireContext().packageName)
            val seriesId = resources.getIdentifier("edit_series_repeticiones$i", "id", requireContext().packageName)
            val pesoId = resources.getIdentifier("edit_peso$i", "id", requireContext().packageName)
            val realizadoId = resources.getIdentifier("checkbox_realizado$i", "id", requireContext().packageName)

            // Verificar que los IDs son válidos
            if (nombreId != 0 && seriesId != 0 && pesoId != 0 && realizadoId != 0) {
                val nombreRutinaEditText: EditText = view.findViewById(nombreId)
                val seriesRepeticionesEditText: EditText = view.findViewById(seriesId)
                val pesoEditText: EditText = view.findViewById(pesoId)
                val realizadoCheckBox: CheckBox = view.findViewById(realizadoId)

                nombreRutinaEditTexts.add(nombreRutinaEditText)
                seriesRepeticionesEditTexts.add(seriesRepeticionesEditText)
                pesoEditTexts.add(pesoEditText)
                realizadoCheckBoxes.add(realizadoCheckBox)
            } else {
                Log.e("RutinaDialogFragment", "No se encontró el ID para la rutina $i")
            }
        }

        // Rellenar los campos si hay rutinas existentes
        rutinasExistentes?.let {
            for (i in it.indices) {
                nombreRutinaEditTexts[i].setText(it[i].nombreRutina)
                seriesRepeticionesEditTexts[i].setText(it[i].seriesRepeticiones)
                pesoEditTexts[i].setText(it[i].peso)
                realizadoCheckBoxes[i].isChecked = it[i].realizado
            }
        }

        builder.setView(view)
            .setTitle("Agregar/Editar Rutinas")
            .setPositiveButton("Guardar") { _: DialogInterface, _: Int ->
                val nuevasRutinas = mutableListOf<Rutina>()
                for (i in 0 until nombreRutinaEditTexts.size) {
                    val nombre = nombreRutinaEditTexts[i].text.toString()
                    val series = seriesRepeticionesEditTexts[i].text.toString()
                    val peso = pesoEditTexts[i].text.toString()
                    val realizado = realizadoCheckBoxes[i].isChecked

                    // Verificar si el usuario ingresó datos en este conjunto
                    if (nombre.isNotBlank() || series.isNotBlank() || peso.isNotBlank()) {
                        val rutina = Rutina(
                            nombreRutina = nombre,
                            seriesRepeticiones = series,
                            peso = peso,
                            realizado = realizado
                        )
                        nuevasRutinas.add(rutina)
                    }
                }
                onRutinasSaved?.invoke(nuevasRutinas)
            }
            .setNegativeButton("Cancelar") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.cancel()
            }

        return builder.create()
    }

    fun setOnRutinasSavedListener(listener: (List<Rutina>) -> Unit) {
        onRutinasSaved = listener
    }

    companion object {
        fun newInstance(rutinasExistentes: MutableList<Rutina>?): RutinaDialogFragment {
            val fragment = RutinaDialogFragment()
            val args = Bundle()
            args.putParcelableArrayList("rutinasExistentes", ArrayList(rutinasExistentes ?: emptyList()))
            fragment.arguments = args
            return fragment
        }
    }
}
