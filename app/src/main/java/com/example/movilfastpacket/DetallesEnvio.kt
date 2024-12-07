package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movilfastpacket.databinding.ActivityDetallesEnvioBinding
import com.example.movilfastpacket.databinding.ActivityMainBinding

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
       binding.textView22.setOnClickListener {
           irPantallaEditarEstadoEnvio()
       }
    }

    fun irPantallaEditarEstadoEnvio(){
        val intent = Intent(this@DetallesEnvio, EstadoEnvio::class.java)
        startActivity(intent)
    }
}