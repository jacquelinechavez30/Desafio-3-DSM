package com.example.digitalsafe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditarrecursosActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var imagenEditText: EditText
    private lateinit var modificarButton: Button

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_editarrecursos)

        tituloEditText = findViewById(R.id.editTextTituloEditar)
        descripcionEditText = findViewById(R.id.editTextDescripcionEditar)
        tipoEditText = findViewById(R.id.editTextTipoEditar)
        enlaceEditText = findViewById(R.id.editTextEnlaceEditar)
        imagenEditText = findViewById(R.id.editTextImagenEditar)
        modificarButton = findViewById(R.id.btnModificarRecurso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getIntExtra("id", -1)
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val tipo = intent.getStringExtra("tipo")
        val enlace = intent.getStringExtra("enlace")
        val imagen = intent.getStringExtra("imagen")

        tituloEditText.setText(titulo)
        descripcionEditText.setText(descripcion)
        tipoEditText.setText(tipo)
        enlaceEditText.setText(enlace)
        imagenEditText.setText(imagen)

        // Botón de cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btncerrarsesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


    }
}