package com.android.wearable.cuandollegawearos.business

import com.android.wearable.cuandollegawearos.network.*

class SeleccionColectivoManager {
    interface Callback<T> {
        fun onSuccess(data: T)
        fun onError(error: String)
    }

    fun obtenerLineas(callback: Callback<List<LineaColectivo>>) {
        RetrofitClient.apiService.obtenerLineas("ACCION_LINEAS").enqueue(object : retrofit2.Callback<LineasResponse> {
            override fun onResponse(call: retrofit2.Call<LineasResponse>, response: retrofit2.Response<LineasResponse>) {
                if (response.isSuccessful) {
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
        RetrofitClient.apiService.obtenerCalles("ACCION_CALLES", codigoLinea).enqueue(object : retrofit2.Callback<CallesResponse> {
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
        RetrofitClient.apiService.obtenerIntersecciones("ACCION_INTERSECCIONES", codigoLinea, codigoCalle).enqueue(object : retrofit2.Callback<InterseccionesResponse> {
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

    fun obtenerSubLineas(codigoLinea: String, codigoCalle: String, codigoInterseccion: String, callback: Callback<List<SubLinea>>) {
        RetrofitClient.apiService.obtenerSubLineas("ACCION_SUBLINEAS", codigoLinea, codigoCalle, codigoInterseccion).enqueue(object : retrofit2.Callback<SubLineasResponse> {
            override fun onResponse(call: retrofit2.Call<SubLineasResponse>, response: retrofit2.Response<SubLineasResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.sublineas ?: emptyList())
                } else {
                    callback.onError("Error al obtener sublíneas")
                }
            }
            override fun onFailure(call: retrofit2.Call<SubLineasResponse>, t: Throwable) {
                callback.onError("Error de red al obtener sublíneas")
            }
        })
    }
} 