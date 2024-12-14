package com.example.movilfastpacket

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding
import com.example.movilfastpacket.poko.Colaborador
import com.example.movilfastpacket.poko.EnvioDetallado
import com.example.movilfastpacket.poko.Mensaje
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.nio.charset.Charset

class Perfil : AppCompatActivity() {

    private lateinit var binding : ActivityPerfilBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        var idColaborador = intent.getStringExtra("idColaborador")
        var noPersonal = intent.getStringExtra("noPersonal")
        binding.btnEditarPerfil.setOnClickListener {
            irPantallaEditarPerfil()
        }

        binding.btnAtrasPerfil.setOnClickListener {
            irAtras()
        }

        obtenerInfoColaborador(noPersonal.toString())
        descargarFotoPerfil(idColaborador.toString())
    }

    private fun descargarFotoPerfil(idColaborador: String) {
        Ion.with(this@Perfil)
            .load("GET", "${Constantes().URL_WS}colaboradores/obtenerFotografia/${idColaborador}")
            .asString()
            .setCallback{ e, result ->
                if (e == null){
                    Log.i("FOTO", result)
                    cargarFotoPerfil(result)
                }else{
                    Toast.makeText(this@Perfil, "Error: "+ e.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun cargarFotoPerfil(jsonFoto : String){
        if (jsonFoto.isNotEmpty()){
            val gson = Gson()
            val colaboradorFoto = gson.fromJson(jsonFoto, Mensaje::class.java)

            if (!colaboradorFoto.contenido.isNullOrEmpty()){
                try {
                    colaborador.fotografia = colaboradorFoto.contenido
                    val imgBytes = Base64.decode(colaboradorFoto.contenido, Base64.DEFAULT)
                    val imgBitMap = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.size)
                    binding.imgFoto.setImageBitmap(imgBitMap)
                }catch (e: Exception){
                    Toast.makeText(this@Perfil, e.message, Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@Perfil, "Agrega una foto de perfil para visualizarla", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun obtenerInfoColaborador(noPersonal: String) {
        Ion.with(this@Perfil)
            .load("GET", "${Constantes().URL_WS}colaboradores/obtenerColaboradorNoPersonal/${noPersonal}")
            .asString(Charset.forName("UTF-8"))
            .setCallback{ e, result ->
                if (e == null){
                    Log.i("JSON: ${noPersonal}", result)
                    colaborador = Gson().fromJson(result, Colaborador::class.java)
                    cargarInfoColaborador(colaborador)
                }else{
                    Toast.makeText(this@Perfil, "Ocurrio un error al obtener los  detalles del envio", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun cargarInfoColaborador(colaborador: Colaborador) {
        var nombreCompleto = colaborador.nombre + " " + colaborador.apellidoPaterno + " " + colaborador.apellidoMaterno
        binding.tvNombre.text = nombreCompleto
        binding.tvCurp.text = colaborador.curp
        binding.tvCorreo.text = colaborador.correo
        binding.tvNoPersonal.text = colaborador.noPersonal
    }

    fun irPantallaEditarPerfil(){
        val intent = Intent(this@Perfil, EditarPerfil::class.java)
        colaborador.fotografia = ""
        var jsonColaborador = Gson().toJson(colaborador)

        intent.putExtra("jsonColaborador", jsonColaborador)
        startActivity(intent)
    }

    fun irAtras(){
        finish()
    }
}