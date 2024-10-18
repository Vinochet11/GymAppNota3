package com.example.jose_leon.Nueva_aplicacion.models


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jose_leon.R


class DiasAdapter (
    private val diasList: List<DiaRutina>,
    private val onAddRutinaClick:(DiaRutina)->Unit
): RecyclerView.Adapter<DiasAdapter.DiaViewHolder>(){

    inner class DiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nombreDiaTextView: TextView=itemView.findViewById(R.id.text_nombre_dia)
        val agregarRutinaButton: Button = itemView.findViewById(R.id.button_agregar_rutina)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dia_rutina,parent,false)
        return DiaViewHolder(view)
    }

    override fun onBindViewHolder(holder:DiaViewHolder,position:Int){
        val dia = diasList[position]
        holder.nombreDiaTextView.text=dia.nombreDia
        holder.agregarRutinaButton.text= if(dia.rutina==null)"Agregar Rutina" else "Editar Rutina"
        holder.agregarRutinaButton.setOnClickListener{
            onAddRutinaClick(dia)
        }
    }
    override fun getItemCount(): Int = diasList.size
}