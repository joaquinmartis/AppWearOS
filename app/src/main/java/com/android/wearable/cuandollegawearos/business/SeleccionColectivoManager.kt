package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * SeleccionColectivoManager es una clase que juega de modelo en el MVC. Maneja la logica de las request HTTP que se crean
 * y el manejo de errores.Contiene una serie de funciones todas referidas a la seleccion de la parada y la linea de colectivo para
 * obtener luego los arribos. Son 4 llamadas a la API. 1 obtiene lineas 2 obtiene calles por las que pasa esa linea
 * 3 obtiene intersecciones por donde pasa esa linea y calle 4) obtiene los destinos de los colectivo(sentidos de circulacion) *
 */
class SeleccionColectivoManager {
    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(error: String)
    }
    private val seleccionRepository: SeleccionRepository = SeleccionRepository

    fun obtenerLineas(callback: Callback<List<LineaColectivo>>) {
        RetrofitClient.apiService.obtenerLineas(EnumAcciones.ACCION_LINEAS.nombreAccion).enqueue(object : retrofit2.Callback<LineasResponseAPI> {
            override fun onResponse(call: Call<LineasResponseAPI>, response: Response<LineasResponseAPI>) {

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        callback.onError("Respuesta vacía del servidor")
                        return
                    }
                    try {
                        val lineas = Mappers.parserAPItoModels(body) // lanza ApiResponseException si codigoEstado != 0

                        if (lineas.isEmpty()) {
                            callback.onError("No hay líneas disponibles")
                            return
                        }

                        callback.onSuccess(lineas)
                    } catch (e: ApiResponseException) {
                        callback.onError("Error ${e.code}: ${e.apiMessage}")
                    } catch (e: Exception) {
                        callback.onError("Error al procesar la respuesta")
                    }
                } else {

                    callback.onError("Error al obtener líneas")
                }
            }
            override fun onFailure(call: Call<LineasResponseAPI>, t: Throwable) {
                callback.onError("Error de red al obtener líneas")
                //Log.e("SELECTION_API", t.toString())
            }
        })
    }

    fun obtenerCalles(codigoLinea: String, callback: Callback<List<Calle>>) {
        RetrofitClient.apiService.obtenerCalles(EnumAcciones.ACCION_CALLES.nombreAccion, codigoLinea).enqueue(object : retrofit2.Callback<CallesResponseAPI> {
            override fun onResponse(call: Call<CallesResponseAPI>, response: Response<CallesResponseAPI>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        callback.onError("Respuesta vacía del servidor")
                        return
                    }
                    try {
                        val calles = Mappers.parserAPItoModels(body) // lanza ApiResponseException si codigoEstado != 0

                        if (calles.isEmpty()) {
                            callback.onError("No hay líneas disponibles")
                            return
                        }

                        callback.onSuccess(calles)
                    } catch (e: ApiResponseException) {
                        callback.onError("Error ${e.code}: ${e.apiMessage}")
                    } catch (e: Exception) {
                        callback.onError("Error al procesar la respuesta")
                    }
                } else {
                    callback.onError("Error al obtener calles")
                }
            }
            override fun onFailure(call: Call<CallesResponseAPI>, t: Throwable) {
                callback.onError("Error de red al obtener calles")
            }
        })
    }

    fun obtenerIntersecciones(codigoLinea: String, codigoCalle: String, callback: Callback<List<Interseccion>>) {
        RetrofitClient.apiService.obtenerIntersecciones(EnumAcciones.ACCION_INTERSECCIONES.nombreAccion, codigoLinea, codigoCalle).enqueue(object : retrofit2.Callback<InterseccionesResponseAPI> {
            override fun onResponse(call: Call<InterseccionesResponseAPI>, response: Response<InterseccionesResponseAPI>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        callback.onError("Respuesta vacía del servidor")
                        return
                    }
                    try {
                        val intersecciones = Mappers.parserAPItoModels(body) // lanza ApiResponseException si codigoEstado != 0

                         if (intersecciones.isEmpty()) {
                             callback.onError("No hay líneas disponibles")
                             return
                         }

                        callback.onSuccess(intersecciones)
                    } catch (e: ApiResponseException) {
                        callback.onError("Error ${e.code}: ${e.apiMessage}")
                    } catch (e: Exception) {
                        callback.onError("Error al procesar la respuesta")
                    }
                } else {
                    callback.onError("Error al obtener intersecciones")
                }
            }
            override fun onFailure(call: Call<InterseccionesResponseAPI>, t: Throwable) {
                callback.onError("Error de red al obtener intersecciones")
            }
        })
    }


    fun obtenerDestinos(codigoLinea: String, codigoCalle: String, codigoInterseccion: String, callback: Callback<List<Destino>>) {
        RetrofitClient.apiService.obtenerDestinos(EnumAcciones.ACCION_DESTINOS.nombreAccion, codigoLinea, codigoCalle, codigoInterseccion).enqueue(object : retrofit2.Callback<DestinosResponseAPI> {
            override fun onResponse(call: Call<DestinosResponseAPI>, response: Response<DestinosResponseAPI>) {
                //Log.d("DESTINOS_API",response.body().toString())
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        callback.onError("Respuesta vacía del servidor")
                        return
                    }
                    try {
                        val destinos = Mappers.parserAPItoModels(body) // lanza ApiResponseException si codigoEstado != 0

                        if (destinos.isEmpty()) {
                            callback.onError("No hay líneas disponibles")
                            return
                        }
                        callback.onSuccess(destinos)
                    } catch (e: ApiResponseException) {
                        callback.onError("Error : ${e.apiMessage}")
                    } catch (e: Exception) {
                        callback.onError("Error al procesar la respuesta")
                    }
                } else {
                    callback.onError("Error al obtener destinos")
                }
            }
            override fun onFailure(call: Call<DestinosResponseAPI>, t: Throwable) {
                callback.onError("Error de red al obtener destinos")
            }
        })
    }
}
