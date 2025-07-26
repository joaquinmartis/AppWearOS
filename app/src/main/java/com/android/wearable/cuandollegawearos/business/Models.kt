package com.android.wearable.cuandollegawearos.business

/**
 * Models es un conjunto de data class. Son todas una especie de adapter contra lo que viene
 * de la API
 */

data class Arribo(
    val descripcionLinea: String,
    val descripcionBandera: String,
    val arribo: String,
    val latitud: Double?,
    val longitud: Double?,
    val latitudParada: Double?,
    val longitudParada: Double?,
    val descripcionCortaBandera: String,
    val descripcionCartelBandera: String,
    val esAdaptado: Boolean,
    val identificadorCoche: String,
    val identificadorChofer: String,
    val desvioHorario: String,
    val ultimaFechaHoraGPS: String,
    val mensajeError: String,
    val codigoLineaParada: String,
    val precision: EnumPrecision
)
data class LineaColectivo(
    val codigo: String,
    val nombre: String,
    val codigoEntidad: String,
)
data class Calle(
    val codigo: String,
    val descripcion: String
)
data class Interseccion(
    val codigo: String,
    val descripcion: String
)
data class Destino(
    val descripcion: String,
    val codigo: String,
    val identificador: String,
    val abreviaturaAmpliadaBandera: String
)

