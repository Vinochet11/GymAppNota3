package com.example.jose_leon.Nueva_aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jose_leon.R

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val nombre=findViewById<AppCompatEditText>(R.id.Nombre_Registrar)
        val apellido=findViewById<AppCompatEditText>(R.id.Apellido_Registrar)
        val correo=findViewById<AppCompatEditText>(R.id.Email)
        val contrasenia= findViewById<AppCompatEditText>(R.id.Contrasenia)
        val fecha_nacimiento=findViewById<AppCompatEditText>(R.id.Fecha_nacimiento)
        val altura=findViewById<AppCompatEditText>(R.id.Altura)
        val btnRegistrar=findViewById<AppCompatButton>(R.id.btnRegistrar)
        
        
        btnRegistrar.setOnClickListener{

            val Nombre= nombre.text.toString()
            val Apellido=apellido.text.toString()
            val Correo= correo.text.toString()
            val Contrasenia=contrasenia.text.toString()
            val Fecha_nacimiento=fecha_nacimiento.text.toString()
            val Altura = altura.text.toString()
            if (Nombre.isNotEmpty() and Apellido.isNotEmpty() and Correo.isNotEmpty() and Contrasenia.isNotEmpty() and Fecha_nacimiento.isNotEmpty() and Altura.isNotEmpty()){

                val intent = Intent(this,RutinaActivity::class.java)
                startActivity(intent);
                Toast.makeText(this,"Cuenta Creada" ,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Falta algun Dato", Toast.LENGTH_SHORT).show()
            }


        }
        
    }
}