package com.example.movilfastpacket

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.movilfastpacket.databinding.ActivityEditarPerfilBinding
import com.example.movilfastpacket.databinding.ActivityPerfilBinding
import com.example.movilfastpacket.poko.Colaborador
import com.example.movilfastpacket.poko.Mensaje
import com.example.movilfastpacket.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.io.ByteArrayOutputStream
import java.io.InputStream

class EditarPerfil : AppCompatActivity() {

    private lateinit var binding : ActivityEditarPerfilBinding
    private lateinit var colaborador: Colaborador
    private  var fotoSeleccionada: String = ""

    private lateinit var colaboradorEdit: Colaborador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        var jsonColaborador = intent.getStringExtra("jsonColaborador")
        colaborador = Gson().fromJson(jsonColaborador, Colaborador::class.java)
        cargarDatosColaborador(colaborador)
        descargarFotoPerfil(colaborador.idColaborador.toString())

        binding.btnEditar.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val apellidoPaterno = binding.etApellidoPaterno.text.toString()
            val apellidoMaterno = binding.etApellidoMaterno.text.toString()
            val curp = binding.etCurp.text.toString()
            val correo = binding.etCorreo.text.toString()
            val password = binding.etPasswordEdit.text.toString()

            if (sonDatosValidos(nombre, apellidoPaterno, apellidoMaterno, curp, correo, password)){
                colaboradorEdit = Colaborador(colaborador.idColaborador, nombre,apellidoPaterno, apellidoMaterno,colaborador.noPersonal, correo, curp, password,
                colaborador.idRol, colaborador.rol, colaborador.fotografia, colaborador.noLicencia)

                editarColaborador(colaboradorEdit)
            }
        }

        binding.btnNuevaFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selecionarFotoPerfil.launch(intent)
        }

        binding.btnAtrasEditar.setOnClickListener {
            finish()
        }

    }

    private fun descargarFotoPerfil(idColaborador: String) {
        Ion.with(this@EditarPerfil)
            .load("GET", "${Constantes().URL_WS}colaboradores/obtenerFotografia/${idColaborador}")
            .asString()
            .setCallback{ e, result ->
                if (e == null){
                    val gson = Gson()
                    val colaboradorFoto = gson.fromJson(result, Mensaje::class.java)
                    colaborador.fotografia = colaboradorFoto.contenido
                }else{
                    Toast.makeText(this@EditarPerfil, "Error: "+ e.message, Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun editarColaborador(colaboradorEdit: Colaborador) {
        val gson = Gson()
        val data = gson.toJson(colaboradorEdit)

        Ion.with(this@EditarPerfil)
            .load("PUT", "${Constantes().URL_WS}colaboradores/modificar")
            .setHeader("Content-Type","application/json")
            .setStringBody(data)
            .asString().setCallback { e, result ->
                if (e == null){
                    Log.i("EDIT", result)
                    obtenerRespuesta(result)
                }else{
                    Toast.makeText(this@EditarPerfil, "Ocurrio un error al editar tu información", Toast.LENGTH_SHORT)
                }
            }
    }

    fun obtenerRespuesta(respuesta : String){
        try {
            val gson = Gson()
            val msj = gson.fromJson(respuesta, Mensaje::class.java)
            Toast.makeText(this@EditarPerfil, msj.contenido, Toast.LENGTH_SHORT).show()
            if(!msj.error){
                finish()
            }
        }catch (e :Error){
            e.printStackTrace()
            Toast.makeText(this@EditarPerfil, "Error al obtener respuesta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarDatosColaborador(colaborador: Colaborador) {
        binding.etNombre.setText(colaborador.nombre)
        binding.etApellidoPaterno.setText(colaborador.apellidoPaterno)
        binding.etApellidoMaterno.setText(colaborador.apellidoMaterno)
        binding.etCurp.setText(colaborador.curp)
        binding.etCorreo.setText(colaborador.correo)
        binding.etPasswordEdit.setText(colaborador.password)
    }

    private val selecionarFotoPerfil = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if (result.resultCode ==  RESULT_OK){
            val data =result.data
            val fotoURI = data?.data
            if (fotoURI != null) {
                binding.imgEditar.setImageURI(fotoURI)
                var fotoBase64 = uriToBase64(fotoURI)
                if (!fotoBase64.toString().isEmpty()){
                    colaborador.fotografia = fotoBase64.toString()
                    Log.i("URI", fotoBase64.toString())
                }
            }
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            // Abre el InputStream desde la URI
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            // Convierte InputStream a un bitmap
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Comprueba que el bitmap no sea nulo
            if (bitmap != null) {
                // Comprime el bitmap para convertirlo a un array de bytes
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                // Convierte el array de bytes a una cadena Base64
                Base64.encodeToString(byteArray, Base64.DEFAULT)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }



    private fun sonDatosValidos(nombre: String, apellidoPaterno: String,
                                apellidoMaterno: String, curp: String,
                                correo: String, password: String) : Boolean
    {
        var valido = true
        if (nombre.isEmpty()){
            valido = false
            binding.etNombre.setError("Ingrese un nombre")
        }
        if (apellidoPaterno.isEmpty()){
            valido = false
            binding.etApellidoPaterno.setError("Ingrese un apellido paterno")
        }
        if (apellidoMaterno.isEmpty()){
            valido = false
            binding.etApellidoMaterno.setError("Ingrese un apellido materno")
        }
        if (curp.isEmpty()){
            valido = false
            binding.etCurp.setError("Ingrese un CURP")
        }else{
            if (curp.length != 18){
                valido = false
                binding.etCurp.setError("CURP invalido")
            }
        }
        if (correo.isEmpty()){
            valido = false
            binding.etCorreo.setError("Ingrese un correo")
        }
        if (password.isEmpty()){
            valido = false
            binding.etPasswordEdit.setError("Ingrese un contraseña")
            if (curp.length < 8){
                valido = false
                binding.etCurp.setError("Contraseña no valida")
            }
        }

        return valido
    }
}