package com.example.movilfastpacket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movilfastpacket.adapter.AdapterPaquete
import com.example.movilfastpacket.databinding.ActivityDetallesEnvioBinding
import com.example.movilfastpacket.poko.EnvioDetallado
import com.example.movilfastpacket.poko.Paquete
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion
import java.nio.charset.Charset

class DetallesEnvio : AppCompatActivity() {

    private lateinit var binding : ActivityDetallesEnvioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesEnvioBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        val idEnvio = intent.getStringExtra("idEnvio")

        obtenerInformacionEnvio(idEnvio.toString())
        cargarPaquetesEnvio(idEnvio.toString())

        binding.btnAtrasEnvio.setOnClickListener {
            irAtras()
        }
    }

    private fun cargarPaquetesEnvio(idEnvio: String) {
        Ion.with(this@DetallesEnvio)
            .load("GET", "${Constantes().URL_WS}paquete/obtenerPaquetesEnvio/${idEnvio}")
            .asString()
            .setCallback { e, result ->
                if (e == null) {
                    Log.i("PAQUETES ${idEnvio}", result)
                    val itemType = object : TypeToken<List<Paquete>>() {}.type
                    val items: List<Paquete> = Gson().fromJson(result, itemType)
                    binding.lvPaquetes.adapter = AdapterPaquete(this, items)
                } else {
                    Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun obtenerInformacionEnvio(idEnvio: String) {

        Ion.with(this@DetallesEnvio)
            .load("GET", "${Constantes().URL_WS}envio/obtenerEnvioPorId/${idEnvio}")
            .asString(Charset.forName("UTF-8"))
            .setCallback{ e, result ->
                if (e == null){
                    Log.i("JSON: ${idEnvio}", result)
                    var detallesEnvio: EnvioDetallado = Gson().fromJson(result, EnvioDetallado::class.java)
                    cargarDetallesEnvio(detallesEnvio)
                }else{
                    Toast.makeText(this@DetallesEnvio, "Ocurrio un error al obtener los  detalles del envio", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun cargarDetallesEnvio(detallesEnvio: EnvioDetallado) {
        binding.tvCliente.text = detallesEnvio.cliente
        binding.tvEstadoEnvio.text = detallesEnvio.estadoEnvio
        var direccionCliente = detallesEnvio.direccionCliente.calle + ", #" + detallesEnvio.direccionCliente.numero + ", "+ detallesEnvio.direccionCliente.colonia + ", C.P. " + detallesEnvio.direccionCliente.codigoPostal
        binding.tvDireccionOrigen.text = direccionCliente
        var direccionDestino = detallesEnvio.direccion.calle + ", #" + detallesEnvio.direccion.numero + ", "+ detallesEnvio.direccion.colonia + ", C.P. " + detallesEnvio.direccion.codigoPostal
        binding.tvDireccionDestino.text = direccionDestino
        var contacto =  detallesEnvio.telefonoCliente + " / " + detallesEnvio.correoCliente
        binding.tvContacto.text = contacto
    }

    private fun irAtras(){
        finish()
    }


}