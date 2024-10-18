//MainActivity
package com.example.jose_leon.Nueva_aplicacion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.jose_leon.R
import com.example.jose_leon.R.layout.activity_first
import android.app.Application
import com.google.firebase.FirebaseApp

class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_first)
        val btnIniciar_sesion= findViewById<AppCompatButton>(R.id.btnIniciar_sesion)
        val Correo= findViewById<AppCompatEditText>(R.id.Correo)
        val contrasena= findViewById<AppCompatEditText>(R.id.contrasena)
        val btnRegistrar=findViewById<AppCompatButton>(R.id.btnRegistrar)



        btnIniciar_sesion.setOnClickListener {

            val correo = Correo.text.toString()
            val contrasenia = contrasena.text.toString()


            if (correo.isNotEmpty() and correo.equals("c1")){
                Log.i("JoseLeon","Boton pulsado ${correo}")
                Toast.makeText(this,"Correo: "+correo+ "" + "contrasenia"+contrasenia ,Toast.LENGTH_SHORT).show()
                val intent = Intent(this,RutinaActivity::class.java)
                startActivity(intent);
            }else{
                Toast.makeText(this,"Falta algun dato",Toast.LENGTH_SHORT).show()
            }

        }
        btnRegistrar.setOnClickListener{

            val intent = Intent(this,RegistrarActivity::class.java)
            startActivity(intent);

        }


    }
}
