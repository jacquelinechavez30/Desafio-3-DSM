package com.example.digitalsafe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditarrecursosActivity : AppCompatActivity() {

    private lateinit var idTextView: TextView
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

        idTextView = findViewById(R.id.editIDTextEditar)
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

        val id = intent.getIntExtra("recursoId", -1)
        val titulo = intent.getStringExtra("recursoTitulo")
        val descripcion = intent.getStringExtra("recursoDescripcion")
        val tipo = intent.getStringExtra("recursoTipo")
        val enlace = intent.getStringExtra("recursoEnlace")
        val imagen = intent.getStringExtra("recursoImagen")

        idTextView.text = id.toString()
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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://6819a22a1ac11556350578a6.mockapi.io/practico3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Apiservice::class.java)

        modificarButton.setOnClickListener {
            val tituloEdit = tituloEditText.text.toString()
            val descripcionEdit = descripcionEditText.text.toString()
            val tipoEdit = tipoEditText.text.toString()
            val enlaceEdit = enlaceEditText.text.toString()
            val imagenEdit = imagenEditText.text.toString()

            if (tituloEdit.isEmpty() || descripcionEdit.isEmpty() || tipoEdit.isEmpty() || enlaceEdit.isEmpty() || imagenEdit.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val recursoActualizado = Recursos(id, tituloEdit, descripcionEdit, tipoEdit, enlaceEdit, imagenEdit)

            val jsonRecursoActualizado = Gson().toJson(recursoActualizado)
            Log.d("API", "JSON enviado: $jsonRecursoActualizado")
            val gson = GsonBuilder().setLenient().create()

            api.actualizarRecurso(id, recursoActualizado).enqueue(object : Callback<Recursos> {
                override fun onResponse(call: Call<Recursos>, response: Response<Recursos>) {
                    if (response.isSuccessful && response.body() != null) {
                        // Si la solicitud es exitosa, mostrar un mensaje de éxito en un Toast
                        Toast.makeText(this@EditarrecursosActivity, "Recurso actualizado correctamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), VerrecursosActivity::class.java)
                        startActivity(i)
                    } else {
                        // Si la respuesta del servidor no es exitosa, manejar el error
                        try {
                            val errorJson = response.errorBody()?.string()
                            val errorObj = JSONObject(errorJson)
                            val errorMessage = errorObj.getString("message")
                            Toast.makeText(this@EditarrecursosActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            // Si no se puede parsear la respuesta del servidor, mostrar un mensaje de error genérico
                            Toast.makeText(this@EditarrecursosActivity, "Error al actualizar el recurso", Toast.LENGTH_SHORT).show()
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<Recursos>, t: Throwable) {
                    // Si la solicitud falla, mostrar un mensaje de error en un Toast
                    Log.e("API", "onFailure : $t")
                    Toast.makeText(this@EditarrecursosActivity, "Error al actualizar el alumno", Toast.LENGTH_SHORT).show()

                    // Si la respuesta JSON está malformada, manejar el error
                    try {
                        val gson = GsonBuilder().setLenient().create()
                        val error = t.message ?: ""
                        val recurso = gson.fromJson(error, Recursos::class.java)
                        // trabajar con el objeto Alumno si se puede parsear
                    } catch (e: JsonSyntaxException) {
                        Log.e("API", "Error al parsear el JSON: ${e.message}")
                    } catch (e: IllegalStateException) {
                        Log.e("API", "Error al parsear el JSON: ${e.message}")
                    }
                }
            })
        }
    }
}