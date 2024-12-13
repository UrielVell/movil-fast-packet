package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding
import com.example.movilfastpacket.poko.Colaborador
import com.example.movilfastpacket.poko.EnvioDetallado
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.nio.charset.Charset

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
        var idColaborador = intent.getStringExtra("idColaborador")
        var noPersonal = intent.getStringExtra("noPersonal")
        binding.btnEditarPerfil.setOnClickListener {
            irPantallaEditarPerfil(idColaborador.toString(), noPersonal.toString())
        }

        binding.btnAtrasPerfil.setOnClickListener {
            irAtras()
        }

        obtenerInfoColaborador(noPersonal.toString())
    }

    private fun obtenerInfoColaborador(noPersonal: String) {
        Ion.with(this@Perfil)
            .load("GET", "${Constantes().URL_WS}colaboradores/obtenerColaboradorNoPersonal/${noPersonal}")
            .asString(Charset.forName("UTF-8"))
            .setCallback{ e, result ->
                if (e == null){
                    Log.i("JSON: ${noPersonal}", result)
                    var colaborador: Colaborador = Gson().fromJson(result, Colaborador::class.java)
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

    fun irPantallaEditarPerfil(idColaborador: String, noPersonal: String){
        val intent = Intent(this@Perfil, EditarPerfil::class.java)
        intent.putExtra("idColaborador", idColaborador)
        intent.putExtra("noPersonal", noPersonal)
        startActivity(intent)
    }

    fun irAtras(){
        finish()
    }
}