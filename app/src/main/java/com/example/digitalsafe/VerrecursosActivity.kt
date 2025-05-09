package com.example.digitalsafe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerrecursosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursosAdapter
    private lateinit var api: Apiservice
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verrecursos)

        val fabAgregar: FloatingActionButton = findViewById(R.id.fab_agregar)
        // Botón de cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btncerrarsesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewRecursos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crea una instancia de Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://6819a22a1ac11556350578a6.mockapi.io/practico3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia en el servicio API
        api = retrofit.create(Apiservice::class.java)

        this.cargarDatos(api)

        // nuevo registro
        fabAgregar.setOnClickListener {
            val intent = Intent(this, AgregarrecursosActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: Apiservice) {
        val call = api.obtenerRecursos()
        call.enqueue(object : Callback<List<Recursos>> {
            override fun onResponse(call: Call<List<Recursos>>, response: Response<List<Recursos>>) {
                if (response.isSuccessful) {
                    val recursos = response.body()
                    if (recursos != null) {
                        adapter = RecursosAdapter(recursos)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener { recurso ->
                            Log.d("VerrecursosActivity", "Recurso clickeado: ${recurso.titulo}" + " ${recurso.descripcion}"
                            + " ${recurso.tipo}" + " ${recurso.enlace}" + " ${recurso.imagen}")

                        }

                        adapter.onModificarClick = { recurso ->
                            val intent = Intent(this@VerrecursosActivity, EditarrecursosActivity::class.java)
                            intent.putExtra("recursoId", recurso.id)
                            intent.putExtra("recursoTitulo", recurso.titulo)
                            intent.putExtra("recursoDescripcion", recurso.descripcion)
                            intent.putExtra("recursoTipo", recurso.tipo)
                            intent.putExtra("recursoEnlace", recurso.enlace)
                            intent.putExtra("recursoImagen", recurso.imagen)
                            startActivity(intent)
                        }

                        adapter.onEliminarClick = { recurso ->
                            // Se manda a llamar al método para eliminar el recurso y se hace la respectiva lógica

                            api.eliminarRecurso(recurso.id).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@VerrecursosActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                                        cargarDatos(api)  // Recarga la lista
                                    } else {
                                        Toast.makeText(this@VerrecursosActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(this@VerrecursosActivity, "Fallo en conexión", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los recursos: $error")
                    Toast.makeText(
                        this@VerrecursosActivity,
                        "Error al obtener los recursos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Recursos>>, t: Throwable) {
                Log.e("API", "Error al obtener los recursos: ${t.message}")
                Toast.makeText(
                    this@VerrecursosActivity,
                    "Error al obtener los recursos",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

}

