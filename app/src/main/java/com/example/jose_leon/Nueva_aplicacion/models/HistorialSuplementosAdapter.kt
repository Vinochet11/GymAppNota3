package com.example.jose_leon.Nueva_aplicacion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.R
import com.example.jose_leon.Nueva_aplicacion.models.Suplemento

class HistorialSuplementosAdapter(
    private val suplementos: List<Suplemento>,
    private val onDeleteClick: (Suplemento) -> Unit // Listener para eliminación
) : RecyclerView.Adapter<HistorialSuplementosAdapter.SuplementoViewHolder>() {

    inner class SuplementoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.text_nombre_suplemento)
        val cantidadTextView: TextView = itemView.findViewById(R.id.text_cantidad_suplemento)
        val deleteButton: Button = itemView.findViewById(R.id.button_eliminar_suplemento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuplementoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suplemento, parent, false)
        return SuplementoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuplementoViewHolder, position: Int) {
        val suplemento = suplementos[position]
        holder.nombreTextView.text = suplemento.nombre
        holder.cantidadTextView.text = suplemento.cantidad.toString()

        // Configurar el botón de eliminación
        holder.deleteButton.setOnClickListener {
            onDeleteClick(suplemento)
        }
    }

    override fun getItemCount(): Int = suplementos.size
}

