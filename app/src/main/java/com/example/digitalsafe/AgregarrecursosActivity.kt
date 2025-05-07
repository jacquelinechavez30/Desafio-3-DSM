package com.example.digitalsafe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgregarrecursosActivity : AppCompatActivity() {
    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var imagenEditText: EditText
    private lateinit var guardarButton: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_agregarrecursos)
        tituloEditText = findViewById(R.id.editTextTitulo)
        descripcionEditText = findViewById(R.id.editTextDescripcion)
        tipoEditText = findViewById(R.id.editTextTipo)
        enlaceEditText = findViewById(R.id.editTextEnlace)
        imagenEditText = findViewById(R.id.editTextImagen)
        guardarButton = findViewById(R.id.btnGuardarRecurso)

        // Botón de cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btncerrarsesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        guardarButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()
            val tipo = tipoEditText.text.toString()
            val enlace = enlaceEditText.text.toString()
            val imagen = imagenEditText.text.toString()
            if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || enlace.isEmpty() || imagen.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recurso = Recursos(0, titulo, descripcion, tipo, enlace, imagen)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://6819a22a1ac11556350578a6.mockapi.io/practico3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(Apiservice::class.java)

            api.crearRecurso(recurso).enqueue(object : Callback<Recursos> {
                override fun onResponse(call: Call<Recursos>, response: Response<Recursos>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AgregarrecursosActivity, "Recurso creado exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AgregarrecursosActivity, VerrecursosActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear recurso: $error")
                        Toast.makeText(this@AgregarrecursosActivity, "Error al crear el recurso", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Recursos>, t: Throwable) {
                    Toast.makeText(this@AgregarrecursosActivity, "Error al crear el recurso: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}