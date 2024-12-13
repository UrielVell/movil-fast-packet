package com.example.movilfastpacket.poko

data class Paquete(
    var idPaquete: Int,
    var idEnvio: Int,
    var descripcion: String,
    var peso: Double,
    var profundidad: Int,
    var altura: Int,
    var ancho: Int
)
