package com.example.jose_leon.Nueva_aplicacion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val botonEliminar: View = view.findViewById(R.id.boton_eliminar) // Asegúrate de que exista en el XML

        init {
            // Configura el evento de clic para el botón de eliminación
            botonEliminar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDelete(calentamientos[position])
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
        val ejercicio = calentamientos[position]
        holder.nombre.text = ejercicio.nombre
        holder.imagen.setImageResource(ejercicio.imagenResId)
    }

    override fun getItemCount(): Int = calentamientos.size
}

