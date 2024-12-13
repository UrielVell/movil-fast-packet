package com.example.movilfastpacket.poko

data class Colaborador(
    var idColaborador: Int,
    var nombre: String,
    var apellidoPaterno: String,
    var apellidoMaterno: String,
    var noPersonal: String,
    var correo: String,
    var curp: String,
    var password: String,
    var idRol: Int,
    var rol: String,
    var fotografia: String ?,
    var noLicencia: String ?
)
