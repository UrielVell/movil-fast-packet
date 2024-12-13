package com.example.movilfastpacket.poko

data class Envio(
    var idEnvio: Int,
    var noGuia: String,
    var costoEnvio: Double,
    var idEstadoEnvio: Int,
    var idCliente: Int ?,
    var idColaborador: Int ?,
    var idDireccionDestino: Int ?,
    var cliente: String ?,
    var conductor: String ?,
    var estatus: String
)
