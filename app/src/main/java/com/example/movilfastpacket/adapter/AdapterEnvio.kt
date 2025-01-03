package com.example.movilfastpacket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movilfastpacket.R
import com.example.movilfastpacket.poko.Envio

class AdapterEnvio (
    private val listaEnvios : List<Envio>,
    private val VerDetallesEnvio: (Envio) -> Unit,
    private val EditarEstadoEnvio: (Envio) -> Unit
    ) : RecyclerView.Adapter<AdapterEnvio.ViewHolder>() {



    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val noGuia: TextView = view.findViewById(R.id.tv_no_guia)
        val estadoEnvio: TextView = view.findViewById(R.id.tv_estado)
        val direccion: TextView = view.findViewById(R.id.tv_direccion)
        val btnVerDetalles: ImageButton = view.findViewById(R.id.btn_detalles_envio)
        val btnEditarEstado: ImageButton = view.findViewById(R.id.btn_editar_envio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_envio, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val envio = listaEnvios[position]
        holder.noGuia.text = envio.noGuia
        holder.estadoEnvio.text = envio.estatus
        holder.direccion.text = envio.direccion

        holder.btnVerDetalles.setOnClickListener {
            VerDetallesEnvio(envio)
        }

        holder.btnEditarEstado.setOnClickListener {
            EditarEstadoEnvio(envio)
        }
    }

    override fun getItemCount(): Int = listaEnvios.size

}