package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.ArriboAPI

// Data class Arribo con lógica relevante

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

        /*// Haversine para calcular distancia en km
        private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val R = 6371 // Radio de la tierra en km
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            return R * c
        }*/

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
