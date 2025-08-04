package com.android.wearable.cuandollegawearos.business
import android.util.Log
import com.android.wearable.cuandollegawearos.network.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.Duration

/**
 * Utils es una clase que contiene funciones utiles para el resto del codigo.
 *
 *
 */

class Utils {
    /***
     * Convierte un String de fecha y hora a un LocalDateTime y lo compara con la hora local actual.
     *  Esta funcion soporta solo intervalos de tiempo pequeños, con intervalos grandes(un mes) podria tener problemas
     * TODO: Tomar la hora actual desde internet y no desde el dispositivo.
     *   @param fechaYHora El String que contiene la fecha y hora a parsear.
     *
     *   @return Enumerado(EnumPrecision) con la precision
     *
     */
    companion object {
        fun calculaPresicion(fechaYHora: String): EnumPrecision {
            val ahora: LocalDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            var precision: EnumPrecision = EnumPrecision.ERROR
            try {
                val fechaHoraParseada: LocalDateTime = LocalDateTime.parse(fechaYHora, formatter)
                val duracion = Duration.between(fechaHoraParseada,ahora)
                val duracionSegundos = duracion.getSeconds()
                if (duracionSegundos < 0 || duracionSegundos > 3600) {
                    throw HoraAPIInvalidaException(
                        fechaHoraParseada.toString() + "es un valor incorrecto" + ahora.toString() + "es la hora actual" + "la duracion es" + duracionSegundos.toString()
                    );
                }
                //Log.d("DEBUG_DURACION", "DURACION EN SEG: "+duracionSegundos.toString()+" LA HORA PROVISTA ES "+fechaHoraParseada.toString() + " la hora actual es " + ahora.toString() )
                if (duracionSegundos < EnumPrecision.ALTA.rango)
                    precision = EnumPrecision.ALTA
                else
                    if (duracionSegundos < EnumPrecision.MEDIA.rango)
                        precision = EnumPrecision.MEDIA
                    else
                        precision = EnumPrecision.BAJA
            } catch (e: DateTimeParseException) {
                //Log.e("ERR_PARSEO", e.message.toString())
            }
            return precision
        }

    }

}

