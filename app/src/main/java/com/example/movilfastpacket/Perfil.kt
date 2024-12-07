package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding

class Perfil : AppCompatActivity() {

    private lateinit var binding : ActivityPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        binding.btnEditarPerfil.setOnClickListener {
            irPantallaEditarPerfil()
        }
    }

    fun irPantallaEditarPerfil(){
        val intent = Intent(this@Perfil, EditarPerfil::class.java)
        startActivity(intent)
    }
}