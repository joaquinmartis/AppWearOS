package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.*

class Mappers {
    companion object{

        fun ArriboAPI.toDomain() = Arribo(
            descripcionLinea = descripcionLinea,
            descripcionBandera = descripcionBandera,
            arribo = arribo,
            latitud = latitud.toDoubleOrNull(),
            longitud = longitud.toDoubleOrNull(),
            latitudParada = latitudParada.toDoubleOrNull(),
            longitudParada = longitudParada.toDoubleOrNull(),
            descripcionCortaBandera = descripcionCortaBandera,
            descripcionCartelBandera = descripcionCartelBandera,
            esAdaptado = esAdaptado.equals("true", ignoreCase = true),
            identificadorCoche = identificadorCoche,
            identificadorChofer = identificadorChofer,
            desvioHorario = desvioHorario,
            ultimaFechaHoraGPS = ultimaFechaHoraGPS,
            mensajeError = mensajeError,
            codigoLineaParada = codigoLineaParada,
            precision = calculaPresiciontiempo(ultimaFechaHoraGPS)
        )

        private fun calculaPresiciontiempo(fechaBondi:String): EnumPrecision{
            var ret: EnumPrecision
            try {
                ret=Utils.calculaPresicion(fechaBondi)
            }catch(e: HoraAPIInvalidaException){
                ret=EnumPrecision.ERROR
                //Log.e("ERR_HORAAPI", e.message.toString())
            }
            return ret
        }
        fun LineaColectivoAPI.toDomain() = LineaColectivo(
            codigo = codigo,
            nombre = nombre,
            codigoEntidad = codigoEntidad
        )

        fun CalleAPI.toDomain(): Calle {
            val (nombreCalle, nombreCiudad) = desarmar(descripcion)
            return Calle(
                codigo = codigo,
                nombreCalle = nombreCalle,
                nombreCiudad = nombreCiudad
            )
        }

        fun InterseccionAPI.toDomain() : Interseccion{
            val (nombreCalle, nombreCiudad) = desarmar(descripcion)
            return Interseccion(
                codigo = codigo,
                nombreInterseccion = nombreCalle,
                nombreCiudad = nombreCiudad
            )
        }

        fun DestinoAPI.toDomain() = Destino(
            descripcion = descripcion,
            codigo = codigo,
            identificador = identificador,
            abreviaturaAmpliadaBandera = abreviaturaAmpliadaBandera
        )

        fun ArribosResponseAPI.toDomainListOrThrow(): List<Arribo> {
            if (codigoEstado != 0) {
                throw ApiResponseException(codigoEstado, mensajeEstado)
            }
            return arribos?.map { it.toDomain() }.orEmpty()
        }
        fun CallesResponseAPI.toDomainListOrThrow(): List<Calle> {
            if (codigoEstado != 0) {
                throw ApiResponseException(codigoEstado, mensajeEstado)
            }
            return calles?.map { it.toDomain() }.orEmpty()
        }

        fun InterseccionesResponseAPI.toDomainListOrThrow(): List<Interseccion> {
            if (codigoEstado != 0) {
                throw ApiResponseException(codigoEstado, mensajeEstado)
            }
            return intersecciones?.map { it.toDomain() }.orEmpty()
        }

        fun DestinosResponseAPI.toDomainListOrThrow(): List<Destino> {
            if (codigoEstado != 0) {
                throw ApiResponseException(codigoEstado, mensajeEstado)
            }
            return destinos?.map { it.toDomain() }.orEmpty()
        }
        fun LineasResponseAPI.toDomainListOrThrow(): List<LineaColectivo> {
            if (codigoEstado != 0) {
                throw ApiResponseException(codigoEstado, mensajeEstado)
            }
            return lineas?.map { it.toDomain() }.orEmpty()
        }

        fun parserAPItoModels(objeto: LineasResponseAPI): List<LineaColectivo> =
            objeto.toDomainListOrThrow()

        fun parserAPItoModels(objeto: CallesResponseAPI): List<Calle> =
            objeto.toDomainListOrThrow()

        fun parserAPItoModels(objeto: InterseccionesResponseAPI): List<Interseccion> =
            objeto.toDomainListOrThrow()

        fun parserAPItoModels(objeto: DestinosResponseAPI): List<Destino> =
            objeto.toDomainListOrThrow()

        fun parserAPItoModels(objeto: ArribosResponseAPI): List<Arribo> =
            objeto.toDomainListOrThrow()

        fun desarmar(str: String) : Pair<String,String>{
            val partes = str.split(" - ")
            return if (partes.size >= 2) {
                val izquierda = partes[0].trim()
                val derecha = partes[1].trim()
                Pair(izquierda, derecha)
            } else {
                Pair("","") // No tiene el formato esperado
            }
        }
    }


}

