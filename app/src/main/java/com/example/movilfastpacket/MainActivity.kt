package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movilfastpacket.databinding.ActivityLoginBinding
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        binding.btnPerfil.setOnClickListener {
            irPantallaPerfil()
        }

        binding.textView5.setOnClickListener {
            irPantallaDetallesEnvio()
        }
    }

    fun irPantallaPerfil(){
        val intent = Intent(this@MainActivity, Perfil::class.java)
        startActivity(intent)
    }

    fun irPantallaDetallesEnvio(){
        val intent = Intent(this@MainActivity, DetallesEnvio::class.java)
        startActivity(intent)
    }
}