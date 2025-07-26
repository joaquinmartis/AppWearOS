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
                Log.e("ERR_HORAAPI", e.message.toString())
            }
            return ret
        }
        fun LineaColectivoAPI.toDomain() = LineaColectivo(
            codigo = codigo,
            nombre = nombre,
            codigoEntidad = codigoEntidad
        )

        fun CalleAPI.toDomain() = Calle(
            codigo = codigo,
            descripcion = descripcion
        )

        fun InterseccionAPI.toDomain() = Interseccion(
            codigo = codigo,
            descripcion = descripcion
        )

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
    }
}

