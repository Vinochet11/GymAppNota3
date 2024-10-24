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
    private val onDelete: (Calentamiento) -> Unit
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
                        onDelete(ejercicios[position])
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

        // Obtener el ID del recurso a partir del nombre
        val resourceId = holder.itemView.context.resources.getIdentifier(
            ejercicio.imagenResourceName, "mipmap", holder.itemView.context.packageName
        )
        if (resourceId != 0) {
            holder.imagen.setImageResource(resourceId)
        } else {
            // Si no se encuentra el recurso, puedes establecer una imagen por defecto o manejar el error
            holder.imagen.setImageResource(R.mipmap.ic_launcher) // Imagen por defecto
        }

        holder.checkBox.isChecked = false
    }

    override fun getItemCount(): Int = ejercicios.size
}
