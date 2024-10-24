package com.example.jose_leon.Nueva_aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.jose_leon.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        auth = FirebaseAuth.getInstance()

        val nombre = findViewById<AppCompatEditText>(R.id.Nombre_Registrar)
        val apellido = findViewById<AppCompatEditText>(R.id.Apellido_Registrar)
        val correo = findViewById<AppCompatEditText>(R.id.Email)
        val contrasena = findViewById<AppCompatEditText>(R.id.Contrasenia)
        val btnRegistrar = findViewById<AppCompatButton>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val email = correo.text.toString()
            val password = contrasena.text.toString()
            val nombreUsuario = nombre.text.toString()
            val apellidoUsuario = apellido.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && nombreUsuario.isNotEmpty() && apellidoUsuario.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val userMap = hashMapOf(
                                "nombre" to nombreUsuario,
                                "apellido" to apellidoUsuario,
                                "email" to email
                            )

                            // Guardar datos adicionales en Firestore
                            if (userId != null) {
                                firestore.collection("usuarios").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, RutinaActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

