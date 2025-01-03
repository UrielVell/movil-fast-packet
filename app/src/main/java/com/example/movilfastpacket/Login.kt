package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movilfastpacket.databinding.ActivityLoginBinding
import com.example.movilfastpacket.poko.LoginColaborador
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class Login : AppCompatActivity() {

    private lateinit var binding :ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i("EE", "dassdas")
    }

    override fun onStart() {
        super.onStart()
        binding.btnIngresar.setOnClickListener {
            val noPersonal = binding.etNoPersonal.text.toString()
            val password = binding.etPassword.text.toString()
            if(camposValidos(noPersonal, password)){
                verificarCredenciales(noPersonal, password)
            }
        }
    }

    fun camposValidos (noPersonal: String, password : String) : Boolean{
        var valido= true
        if (noPersonal.isEmpty()){
            valido = false
            binding.etNoPersonal.setError("Ingrese un No. de personal")
        }
        if (password.isEmpty()){
            valido = false
            binding.etPassword.setError("Ingrese una contrasena")
        }
        return valido
    }

    fun verificarCredenciales (noPersonal: String, password : String){
        Ion.getDefault(this@Login).conscryptMiddleware.enable(false)

        Ion.with(this@Login)
            .load("POST", "${Constantes().URL_WS}autenticacion/loginMovil")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("noPersonal", noPersonal)
            .setBodyParameter("password", password)
            .asString()
            .setCallback{ e, result ->
                if (e == null){
                    serializarRespuesta(result)
                }else{
                    Toast.makeText(this@Login, "Error: "+ e.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun serializarRespuesta(json : String){
        val gson = Gson()
        val resultadoLogin = gson.fromJson(json, LoginColaborador::class.java)
        Toast.makeText(this@Login, resultadoLogin.mensaje, Toast.LENGTH_LONG).show()
        if (!resultadoLogin.error){
            irPantallaPrincipal(resultadoLogin.colaboradorSesion.idColaborador, resultadoLogin.colaboradorSesion.noPersonal)
        }
    }

    fun irPantallaPrincipal(idColaborador: Int, noPersonal: String){
        val intent = Intent(this@Login, MainActivity::class.java)
        intent.putExtra("idColaborador", idColaborador.toString())
        intent.putExtra("noPersonal", noPersonal)
        startActivity(intent)
        finish()
    }


}