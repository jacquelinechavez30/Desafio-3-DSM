package com.example.digitalsafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalsafe.Recursos
import com.squareup.picasso.Picasso


class RecursosAdapter(private val recursos: List<Recursos>) :
    RecyclerView.Adapter<RecursosAdapter.ViewHolder>() {

    private var onItemClick: ((Recursos) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.tvIdRecurso)
        val tituloTextView: TextView = itemView.findViewById(R.id.tvTituloRecurso)
        val descripcionTextView: TextView = itemView.findViewById(R.id.tvDescripcionRecurso)
        val tipoTextView: TextView = itemView.findViewById(R.id.tvTipoRecurso)
        val enlaceTextView: TextView = itemView.findViewById(R.id.tvEnlaceRecurso)
        val imagenImageView: ImageView = itemView.findViewById(R.id.ivImagenRecurso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recursos_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recurso = recursos[position]
        //holder.idTextView.text = recurso.id.toString()
        holder.tituloTextView.text = recurso.titulo
        holder.descripcionTextView.text = recurso.descripcion
        holder.tipoTextView.text = recurso.tipo
        holder.enlaceTextView.text = recurso.enlace

        if (!recurso.imagen.isNullOrEmpty()) {
            Picasso.get().load(recurso.imagen).into(holder.imagenImageView)
            holder.imagenImageView.visibility = View.VISIBLE
        } else {
            holder.imagenImageView.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recurso)
        }
    }

    override fun getItemCount(): Int {
        return recursos.size
    }

    fun setOnItemClickListener(listener: (Recursos) -> Unit) {
        onItemClick = listener
    }
}