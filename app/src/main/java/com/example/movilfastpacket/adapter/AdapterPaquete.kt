package com.example.movilfastpacket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.movilfastpacket.R

import com.example.movilfastpacket.poko.Paquete

class AdapterPaquete (context: Context, private val data: List<Paquete>) : ArrayAdapter<Paquete>(context, R.layout.item_paquete, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_paquete, parent, false)
        val paquete = data[position]

        val descripcion = view.findViewById<TextView>(R.id.tv_descripcion_paquete)
        val dimensiones = view.findViewById<TextView>(R.id.tv_dimensiones)

        descripcion.text = paquete.descripcion
        var dimesionesPaquete = paquete.altura.toString() + "x" + paquete.peso.toString()+"x"+paquete.profundidad.toString()
        dimensiones.text = dimesionesPaquete

        return view
    }
}