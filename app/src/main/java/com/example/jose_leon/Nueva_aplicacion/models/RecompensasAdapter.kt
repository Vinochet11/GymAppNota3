// Archivo: RecompensasAdapter.kt
// Archivo: RecompensasAdapter.kt
package com.example.jose_leon.Nueva_aplicacion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jose_leon.Nueva_aplicacion.models.Recompensa
import com.example.jose_leon.R

class RecompensasAdapter(
    private val recompensas: List<Recompensa>,
    private val unlockedRewards: List<String>
) : RecyclerView.Adapter<RecompensasAdapter.RecompensaViewHolder>() {

    inner class RecompensaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageRecompensa: ImageView = itemView.findViewById(R.id.imageRecompensa)
        val textNombreRecompensa: TextView = itemView.findViewById(R.id.textNombreRecompensa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecompensaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recompensa, parent, false)
        return RecompensaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecompensaViewHolder, position: Int) {
        val recompensa = recompensas[position]
        holder.textNombreRecompensa.text = recompensa.nombre

        if (unlockedRewards.contains(recompensa.id)) {
            // Trofeo desbloqueado: Mostrar la imagen real
            Glide.with(holder.itemView.context)
                .load(recompensa.imagenUrl)
                .placeholder(R.drawable.suplemento_vector)
                .error(R.drawable.calentamiento_vector)
                .into(holder.imageRecompensa)
        } else {
            // Trofeo bloqueado: Mostrar una imagen de trofeo bloqueado
            holder.imageRecompensa.setImageResource(R.drawable.trophy_locked)
        }
    }

    override fun getItemCount(): Int = recompensas.size
}

