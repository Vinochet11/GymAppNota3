package com.example.jose_leon.Nueva_aplicacion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jose_leon.R
import com.example.jose_leon.Nueva_aplicacion.fragments.RutinaFragment
import com.example.jose_leon.Nueva_aplicacion.fragments.SuplementosFragment
import com.example.jose_leon.Nueva_aplicacion.fragments.CalentamientoFragment
import com.example.jose_leon.Nueva_aplicacion.fragments.RecompensaFragment
import com.example.jose_leon.Nueva_aplicacion.fragments.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RutinaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutina)

        val rutinaFragment = RutinaFragment()
        val suplementosFragment = SuplementosFragment()
        val calentamientoFragment = CalentamientoFragment()
        val recompensaFragment = RecompensaFragment()
        val perfilFragment = PerfilFragment()

        makeCurrentFragment(rutinaFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Rutina -> makeCurrentFragment(rutinaFragment)
                R.id.Suplementos -> makeCurrentFragment(suplementosFragment)
                R.id.Calentamiento -> makeCurrentFragment(calentamientoFragment)
                R.id.Recompensa -> makeCurrentFragment(recompensaFragment)
                R.id.Perfil -> makeCurrentFragment(perfilFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
