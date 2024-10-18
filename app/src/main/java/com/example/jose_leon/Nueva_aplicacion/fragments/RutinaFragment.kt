package com.example.jose_leon.Nueva_aplicacion.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jose_leon.R
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.jose_leon.Nueva_aplicacion.models.RutinaPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator


class RutinaFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var fabAgregarRutina: FloatingActionButton

    // orden correcto de las semanas
    private val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rutina, container, false)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        fabAgregarRutina = view.findViewById(R.id.fab_agregar_rutina)

        // adaptador con la lista corresta de dias
        val adapter = RutinaPagerAdapter(this, diasSemana)
        viewPager.adapter = adapter

        // TabLayout con el ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = diasSemana[position]
        }.attach()

        fabAgregarRutina.setOnClickListener {
            val currentFragment = childFragmentManager.findFragmentByTag("f${viewPager.currentItem}") as? DiaRutinaFragment
            currentFragment?.agregarOEditarRutina()
        }

        return view
    }
}


