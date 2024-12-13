package com.example.movilfastpacket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movilfastpacket.adapter.AdapterEnvio
import com.example.movilfastpacket.databinding.ActivityLoginBinding
import com.example.movilfastpacket.databinding.ActivityMainBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding
import com.example.movilfastpacket.poko.Colaborador
import com.example.movilfastpacket.poko.Envio
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

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
        val idColaborador = intent.getStringExtra("idColaborador")
        val noPersonal = intent.getStringExtra("noPersonal")

        binding.rvEnvios.layoutManager = LinearLayoutManager(this)

        binding.btnPerfil.setOnClickListener {
            irPantallaPerfil(idColaborador.toString(), noPersonal.toString())
        }

        cargarEnvios(idColaborador.toString())
    }

    fun cargarEnvios(idColaborador: String){
        Ion.with(this@MainActivity)
            .load("GET", "${Constantes().URL_WS}envio/enviosPorConductor/${idColaborador}")
            .asString()
            .setCallback{ e, result ->
                if (e == null){
                    Log.i("JSON", result)
                    val itemType = object : TypeToken<List<Envio>>() {}.type
                    val items: List<Envio> = Gson().fromJson(result, itemType)
                    binding.rvEnvios.adapter = AdapterEnvio(items,
                        VerDetallesEnvio = { envio ->
                            irPantallaDetallesEnvio(envio.idEnvio)
                        },
                        EditarEstadoEnvio = { envio ->
                            irPanatallaEditarEstadoEnvio(envio.idEnvio)
                        }
                        )
                }else{
                    Toast.makeText(this@MainActivity, "Ocurrio un error al obtener lso envios", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun irPantallaPerfil(idColaborador: String, noPersonal: String){
        val intent = Intent(this@MainActivity, Perfil::class.java)
        intent.putExtra("idColaborador", idColaborador)
        intent.putExtra("noPersonal", noPersonal)
        startActivity(intent)
    }

    fun irPantallaDetallesEnvio(idEnvio: Int){
        val intent = Intent(this@MainActivity, DetallesEnvio::class.java)
        intent.putExtra("idEnvio", idEnvio.toString())
        startActivity(intent)
    }

    fun irPanatallaEditarEstadoEnvio(idEnvio: Int){
        val intent = Intent(this@MainActivity, EstadoEnvio::class.java)
        intent.putExtra("idEnvio", idEnvio)
        startActivity(intent)
    }
}