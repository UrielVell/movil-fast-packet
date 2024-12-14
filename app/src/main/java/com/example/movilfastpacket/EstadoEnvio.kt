package com.example.movilfastpacket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movilfastpacket.databinding.ActivityEstadoEnvioBinding
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.poko.Envio
import com.google.gson.Gson

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
        envio = Gson().fromJson(jsonEnvio, Envio::class.java)
        cargarDatosEnvio(envio)
    }

    private fun cargarDatosEnvio(envio: Envio) {
        binding.tvEnvio.setText("Seleccione el nuevo estado del envio con el No. de Guia: ${envio.noGuia}")
        if(envio.idEstadoEnvio == 1){
            binding.radioGroup.check(binding.rbTransito.id)
        }
        if(envio.idEstadoEnvio == 2){
            binding.radioGroup.check(binding.rbDetenido.id)
        }
        if(envio.idEstadoEnvio == 3){
            binding.radioGroup.check(binding.rbEntregado.id)
        }
        if(envio.idEstadoEnvio == 4){
            binding.radioGroup.check(binding.rbCancelado.id)
        }


    }
}