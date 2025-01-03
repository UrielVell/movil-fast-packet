package com.example.movilfastpacket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movilfastpacket.databinding.ActivityEstadoEnvioBinding
import com.example.movilfastpacket.poko.ActualizarEstado
import com.example.movilfastpacket.poko.Envio
import com.example.movilfastpacket.poko.Mensaje
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class EstadoEnvio : AppCompatActivity() {

    private lateinit var binding: ActivityEstadoEnvioBinding
    private lateinit var envio: Envio
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstadoEnvioBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        var jsonEnvio = intent.getStringExtra("envio")
        var idColaborador = intent.getStringExtra("idColaborador")
        envio = Gson().fromJson(jsonEnvio, Envio::class.java)
        cargarDatosEnvio(envio)

        binding.btnAtrasEstadoEnvio.setOnClickListener {
            finish()
        }

        binding.btnGuardarEstatus.setOnClickListener {
            var comentarios = binding.etComentarios.text.toString()
            var estadoSeleccionado = 1
            if (binding.rbTransito.isChecked){
                estadoSeleccionado = 2
            }
            if (binding.rbDetenido.isChecked){
                estadoSeleccionado = 3
            }
            if (binding.rbEntregado.isChecked){
                estadoSeleccionado = 4
            }
            if (binding.rbCancelado.isChecked){
                estadoSeleccionado = 5
            }
            if(validarEstado(envio.idEstadoEnvio,comentarios, estadoSeleccionado)){
                actualizarEstado(envio.idEnvio, idColaborador.toString(), estadoSeleccionado, comentarios )
            }
        }
    }

    private fun actualizarEstado(idEnvio: Int, idColaborador: String, estadoSeleccionado: Int, comentarios: String) {
        var envio  =  ActualizarEstado(idEnvio, idColaborador, estadoSeleccionado, comentarios)
        var data = Gson().toJson(envio)
        Ion.with(this@EstadoEnvio)
            .load("PUT", "${Constantes().URL_WS}envio/editarEstatus")
            .setHeader("Content-Type","application/json")
            .setStringBody(data)
            .asString().setCallback { e, result ->
                if (e == null){
                    obtenerRespuesta(result)
                }else{
                    Toast.makeText(this@EstadoEnvio, "Ocurrio un error al editar tu información", Toast.LENGTH_SHORT)
                }
            }
    }

    fun obtenerRespuesta(respuesta : String){
        try {
            val gson = Gson()
            val msj = gson.fromJson(respuesta, Mensaje::class.java)
            Toast.makeText(this@EstadoEnvio, msj.contenido, Toast.LENGTH_SHORT).show()
            if(!msj.error){
                finish()
            }
        }catch (e :Error){
            e.printStackTrace()
            Toast.makeText(this@EstadoEnvio, "Error al obtener respuesta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarEstado(idEstadoEnvio: Int, comentarios: String, estadoSeleccionado: Int): Boolean {

        if ((estadoSeleccionado == 3 || estadoSeleccionado == 5) && comentarios.isEmpty()) {
            Toast.makeText(this@EstadoEnvio, "Ingrese un comentario", Toast.LENGTH_SHORT).show()
            return false
        }
        if (idEstadoEnvio >= estadoSeleccionado) {
            Toast.makeText(this@EstadoEnvio, "No se puede actualizar al estado seleccionado", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun cargarDatosEnvio(envio: Envio) {
        binding.tvEnvio.setText("Seleccione el nuevo estado del envio con el No. de Guia: ${envio.noGuia}")
        if(envio.idEstadoEnvio == 1){
            binding.radioGroup.check(binding.rbPendiente.id)
        }
        if(envio.idEstadoEnvio == 2){
            binding.radioGroup.check(binding.rbTransito.id)
        }
        if(envio.idEstadoEnvio == 3){
            binding.radioGroup.check(binding.rbDetenido.id)

        }
        if(envio.idEstadoEnvio == 4){
            binding.radioGroup.check(binding.rbEntregado.id)
            binding.btnGuardarEstatus.isEnabled = false
        }
        if(envio.idEstadoEnvio == 5){
            binding.radioGroup.check(binding.rbCancelado.id)
            binding.btnGuardarEstatus.isEnabled = false
        }
    }
}