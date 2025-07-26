package com.android.wearable.cuandollegawearos.network

import com.google.gson.annotations.SerializedName

/**
 * ApiModels es un conjunto de data class(clases que no hacen nada, solo almacenan datos) con todas las posibles respuestas
 * de la API y su parseo, las dataclass vienen de a pares, pues una tiene la respuesta con el codigo de estado y mensaje, y
 * por otro lado estan los datos que interesan
 *
 *
 */
data class ArribosResponseAPI(
    @SerializedName("CodigoEstado") val codigoEstado: Int,
    @SerializedName("MensajeEstado") val mensajeEstado: String,
    @SerializedName("arribos") val arribos: List<ArriboAPI>?
)

data class ArriboAPI(
    @SerializedName("DescripcionLinea") val descripcionLinea: String,
    @SerializedName("DescripcionBandera") val descripcionBandera: String,
    @SerializedName("Arribo") val arribo: String,
    @SerializedName("Latitud") val latitud: String,
    @SerializedName("Longitud") val longitud: String,
    @SerializedName("LatitudParada") val latitudParada: String,
    @SerializedName("LongitudParada") val longitudParada: String,
    @SerializedName("DescripcionCortaBandera") val descripcionCortaBandera: String,
    @SerializedName("DescripcionCartelBandera") val descripcionCartelBandera: String,
    @SerializedName("EsAdaptado") val esAdaptado: String,
    @SerializedName("IdentificadorCoche") val identificadorCoche: String,
    @SerializedName("IdentificadorChofer") val identificadorChofer: String,
    @SerializedName("DesvioHorario") val desvioHorario: String,
    @SerializedName("UltimaFechaHoraGPS") val ultimaFechaHoraGPS: String,
    @SerializedName("MensajeError") val mensajeError: String,
    @SerializedName("CodigoLineaParada") val codigoLineaParada: String
)

data class LineasResponseAPI(
    @SerializedName("CodigoEstado") val codigoEstado: Int,
    @SerializedName("MensajeEstado") val mensajeEstado: String,
    @SerializedName("lineas") val lineas: List<LineaColectivoAPI>?
)

data class LineaColectivoAPI(
    @SerializedName("CodigoLineaParada") val codigo: String,
    @SerializedName("Descripcion") val nombre: String,
    @SerializedName("CodigoEntidad") val codigoEntidad: String,
    @SerializedName("CodigoEmpresa") val codigoEmpresa: String
)


data class CallesResponseAPI(
    @SerializedName("CodigoEstado") val codigoEstado: Int,
    @SerializedName("MensajeEstado") val mensajeEstado: String,
    @SerializedName("calles") val calles: List<CalleAPI>?
)

data class CalleAPI(
    @SerializedName("Codigo") val codigo: String,
    @SerializedName("Descripcion") val descripcion: String
)

data class InterseccionesResponseAPI(
    @SerializedName("CodigoEstado") val codigoEstado: Int,
    @SerializedName("MensajeEstado") val mensajeEstado: String,
    @SerializedName("calles") val intersecciones: List<InterseccionAPI>?
)

data class InterseccionAPI(
    @SerializedName("Codigo") val codigo: String,
    @SerializedName("Descripcion") val descripcion: String
)

data class DestinoAPI(
    @SerializedName("Codigo") val codigo: String,
    @SerializedName("Identificador") val identificador: String,
    @SerializedName("Descripcion") val descripcion: String,
    @SerializedName("AbreviaturaBandera") val abreviaturaBandera: String,
    @SerializedName("AbreviaturaAmpliadaBandera") val abreviaturaAmpliadaBandera: String,
    @SerializedName("LatitudParada") val latitudParada: String,
    @SerializedName("LongitudParada") val longitudParada: String
)

data class DestinosResponseAPI(
    @SerializedName("CodigoEstado") val codigoEstado: Int,
    @SerializedName("MensajeEstado") val mensajeEstado: String,
    @SerializedName("paradas") val destinos: List<DestinoAPI>?
)
