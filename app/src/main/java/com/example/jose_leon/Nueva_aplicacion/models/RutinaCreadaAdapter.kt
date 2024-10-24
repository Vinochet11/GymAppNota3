package com.example.jose_leon.Nueva_aplicacion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.Nueva_aplicacion.models.Calentamiento
import com.example.jose_leon.R

class RutinaCreadaAdapter(
    private val calentamientos: List<Calentamiento>,
    private val onDelete: (Calentamiento) -> Unit
) : RecyclerView.Adapter<RutinaCreadaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombre_calentamiento)
        val imagen: ImageView = view.findViewById(R.id.imagen_calentamiento)
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_calentamiento)

        init {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onDelete(calentamientos[position])
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
        val calentamiento = calentamientos[position]
        holder.nombre.text = calentamiento.nombre

        // Si usas imágenes de recursos locales:
        val resourceId = holder.itemView.context.resources.getIdentifier(
            calentamiento.imagenResourceName, "mipmap", holder.itemView.context.packageName
        )
        if (resourceId != 0) {
            holder.imagen.setImageResource(resourceId)
        } else {
            holder.imagen.setImageResource(R.mipmap.ic_launcher) // Imagen por defecto
        }

        holder.checkBox.isChecked = false
    }

    override fun getItemCount(): Int = calentamientos.size
}
