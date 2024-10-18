package com.example.jose_leon.Nueva_aplicacion.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.R

class CalentamientoAdapter(
    private val ejercicios: List<Calentamiento>,
    private val onDelete: (Calentamiento) -> Unit // Cambiado a un solo parámetro
) : RecyclerView.Adapter<CalentamientoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombre_calentamiento)
        val imagen: ImageView = view.findViewById(R.id.imagen_calentamiento)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_calentamiento)

        init {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onDelete(ejercicios[position]) // Callback de eliminación
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calentamiento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ejercicio = ejercicios[position]
        holder.nombre.text = ejercicio.nombre
        holder.imagen.setImageResource(ejercicio.imagenResId)
        holder.checkBox.isChecked = false // Reiniciar estado del CheckBox
    }

    override fun getItemCount(): Int = ejercicios.size
}


