package com.example.movilfastpacket.poko

data class EnvioDetallado(
    var cliente: String,
    var conductor: String,
    var estadoEnvio: String,
    var direccion: Direccion,
    var direccionCliente: Direccion,
    var envio: Envio,
    var telefonoCliente: String,
    var correoCliente: String
)
