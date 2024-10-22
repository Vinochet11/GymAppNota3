package com.example.jose_leon.Nueva_aplicacion.models

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jose_leon.Nueva_aplicacion.fragments.DiaRutinaFragment

class RutinaPagerAdapter(
    fragment: Fragment,
    private val diasSemana: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = diasSemana.size

    override fun createFragment(position: Int): Fragment {
        val fragment = DiaRutinaFragment()
        fragment.arguments = Bundle().apply {
            putString("diaSemana", diasSemana[position])
        }
        return fragment
    }
}