package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movilfastpacket.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding :ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        binding.btnIngresar.setOnClickListener {
            irPantallaPrincipal()
        }
    }

    fun irPantallaPrincipal(){
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}