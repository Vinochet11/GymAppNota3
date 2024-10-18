//MainActivity
package com.example.jose_leon.Nueva_aplicacion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.jose_leon.R
import com.google.firebase.auth.FirebaseAuth

class FirstActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        auth = FirebaseAuth.getInstance()

        val btnIniciarSesion = findViewById<AppCompatButton>(R.id.btnIniciar_sesion)
        val correo = findViewById<AppCompatEditText>(R.id.Correo)
        val contrasena = findViewById<AppCompatEditText>(R.id.contrasena)
        val btnRegistrar = findViewById<AppCompatButton>(R.id.btnRegistrar)

        btnIniciarSesion.setOnClickListener {
            val email = correo.text.toString()
            val password = contrasena.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, RutinaActivity::class.java))
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }
    }
}
