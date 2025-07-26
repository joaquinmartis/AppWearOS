package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.*


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
        RetrofitClient.apiService.obtenerLineas(EnumAcciones.ACCION_LINEAS.nombreAccion).enqueue(object : retrofit2.Callback<LineasResponse> {
            override fun onResponse(call: retrofit2.Call<LineasResponse>, response: retrofit2.Response<LineasResponse>) {

                if (response.isSuccessful && (response.body()?.lineas?.isEmpty()==false)) {

                    callback.onSuccess(response.body()?.lineas ?: emptyList())
                } else {
                    callback.onError("Error al obtener líneas")
                }
            }
            override fun onFailure(call: retrofit2.Call<LineasResponse>, t: Throwable) {
                callback.onError("Error de red al obtener líneas")
            }
        })
    }

    fun obtenerCalles(codigoLinea: String, callback: Callback<List<Calle>>) {
        RetrofitClient.apiService.obtenerCalles(EnumAcciones.ACCION_CALLES.nombreAccion, codigoLinea).enqueue(object : retrofit2.Callback<CallesResponse> {
            override fun onResponse(call: retrofit2.Call<CallesResponse>, response: retrofit2.Response<CallesResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.calles ?: emptyList())
                } else {
                    callback.onError("Error al obtener calles")
                }
            }
            override fun onFailure(call: retrofit2.Call<CallesResponse>, t: Throwable) {
                callback.onError("Error de red al obtener calles")
            }
        })
    }

    fun obtenerIntersecciones(codigoLinea: String, codigoCalle: String, callback: Callback<List<Interseccion>>) {
        RetrofitClient.apiService.obtenerIntersecciones(EnumAcciones.ACCION_INTERSECCIONES.nombreAccion, codigoLinea, codigoCalle).enqueue(object : retrofit2.Callback<InterseccionesResponse> {
            override fun onResponse(call: retrofit2.Call<InterseccionesResponse>, response: retrofit2.Response<InterseccionesResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.intersecciones ?: emptyList())
                } else {
                    callback.onError("Error al obtener intersecciones")
                }
            }
            override fun onFailure(call: retrofit2.Call<InterseccionesResponse>, t: Throwable) {
                callback.onError("Error de red al obtener intersecciones")
            }
        })
    }

    fun obtenerDestinos(codigoLinea: String, codigoCalle: String, codigoInterseccion: String, callback: Callback<List<Destino>>) {
        RetrofitClient.apiService.obtenerDestinos(EnumAcciones.ACCION_DESTINOS.nombreAccion, codigoLinea, codigoCalle, codigoInterseccion).enqueue(object : retrofit2.Callback<DestinosResponse> {
            override fun onResponse(call: retrofit2.Call<DestinosResponse>, response: retrofit2.Response<DestinosResponse>) {
                Log.d("DESTINOS_API",response.body().toString())
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.destinos ?: emptyList())
                } else {
                    callback.onError("Error al obtener destinos")
                }
            }
            override fun onFailure(call: retrofit2.Call<DestinosResponse>, t: Throwable) {
                callback.onError("Error de red al obtener destinos")
            }
        })
    }
}
