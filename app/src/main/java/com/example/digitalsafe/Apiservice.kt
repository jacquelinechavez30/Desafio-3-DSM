package com.example.digitalsafe

import retrofit2.Call
import retrofit2.http.*

interface Apiservice {
    @GET("/recursos")
    fun obtenerRecursos(): Call<List<Recursos>>
    @POST("/recursos")
    fun crearRecurso(@Body recurso: Recursos): Call<Recursos>
    @PUT("/recursos/{id}")
    fun actualizarRecurso(@Path("id") id: Int, @Body recurso: Recursos): Call<Recursos>
    @DELETE("/recursos/{id}")
    fun eliminarRecurso(@Path("id") id: Int): Call<Void>
}