package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.ArriboAPI

/***
 * Arribo es una data class con todo lo recibido desde la API y ademas tiene un poco de logica para cosas como la presicion(en tiempo)
 * Ademas hay logica referida a si el bondi viene atrasado, es decir que va mas "rapido" o mas "lento"
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
) {
    // Precisión basada en la diferencia entre posición del coche y la parada
    /* fun precision(): EnumPrecision {
        if (latitud != null && longitud != null && latitudParada != null && longitudParada != null) {
            val distancia = haversine(latitud, longitud, latitudParada, longitudParada)
            return when {
                distancia < 0.1 -> EnumPrecision.ALTA
                distancia < 0.5 -> EnumPrecision.MEDIA
                distancia < 2.0 -> EnumPrecision.BAJA
                else -> EnumPrecision.ERROR
            }
        }
        return EnumPrecision.ERROR
    }

    // Desvío horario como minutos (si es posible)
    fun desvioEnMinutos(): Int? {
        return try {
            val regex = "([+-]?)(\\d{2}):(\\d{2})".toRegex()
            val match = regex.matchEntire(desvioHorario)
            if (match != null) {
                val sign = if (match.groupValues[1] == "-") -1 else 1
                val horas = match.groupValues[2].toInt()
                val minutos = match.groupValues[3].toInt()
                sign * (horas * 60 + minutos)
            } else null
        } catch (e: Exception) { null }
    }*/

    companion object {
        // Conversión desde ArriboAPI
        fun fromApi(api: ArriboAPI): Arribo = Arribo(
            descripcionLinea = api.descripcionLinea,
            descripcionBandera = api.descripcionBandera,
            arribo = api.arribo,
            latitud = api.latitud.toDoubleOrNull(),
            longitud = api.longitud.toDoubleOrNull(),
            latitudParada = api.latitudParada.toDoubleOrNull(),
            longitudParada = api.longitudParada.toDoubleOrNull(),
            descripcionCortaBandera = api.descripcionCortaBandera,
            descripcionCartelBandera = api.descripcionCartelBandera,
            esAdaptado = api.esAdaptado.equals("true", ignoreCase = true),
            identificadorCoche = api.identificadorCoche,
            identificadorChofer = api.identificadorChofer,
            desvioHorario = api.desvioHorario,
            ultimaFechaHoraGPS = api.ultimaFechaHoraGPS,
            mensajeError = api.mensajeError,
            codigoLineaParada = api.codigoLineaParada,
            precision = calculaPresiciontiempo(api.ultimaFechaHoraGPS)
        )

        private fun calculaPresiciontiempo(fechaBondi:String): EnumPrecision{
            var ret: EnumPrecision
            try {
                ret=Utils.calculaPresicion(fechaBondi)
            }catch(e: HoraAPIInvalidaException){
                ret=EnumPrecision.ERROR
                Log.e("ERR_HORAAPI", e.message.toString())
            }
            return ret

        }
    }
}
