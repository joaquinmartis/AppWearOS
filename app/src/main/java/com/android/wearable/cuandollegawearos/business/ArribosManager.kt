package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.PostResponse
import com.android.wearable.cuandollegawearos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * ArribosManager es una clase que juega de modelo en el MVC. Maneja la logica de las request HTTP que se crean
 * y el manejo de errores.
 *
 */

class ArribosManager {
    interface Listener {
        fun onArribosActualizados(arribos: List<Arribo>)
        fun onError(error: String)
        fun onLoading()
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }


    /**
     * Esta funcion se encarga de hacer la solicitud a la api de los arribos de colectivos en una parada y una determinada
     * linea de colectivo. Actualiza la UI dinamicamente, por eso tiene listeners,
     * @param idParada: id de la parada que el usuario marco
     * @param codLineaParada: el codigo de la linea de colectivo que el usuario marco
     *
     */
    fun cargarArribos(idParada: String, codLineaParada: String) {
        listener?.onLoading()
        val call = RetrofitClient.apiService.sendPost(
            accion = "RecuperarProximosArribosW",
            identificadorParada = idParada,
            codigoLineaParada = codLineaParada
        )

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val postResponse = response.body()
                    if (postResponse != null && postResponse.codigoEstado == 0) {
                        val arribos = postResponse.arribos?.map { Arribo.fromApi(it) } ?: emptyList()
                        listener?.onArribosActualizados(arribos)
                        Log.d("ARRIBO_SCREEN", "Arribos actualizados: ${arribos.size}")
                    } else {
                        val errorMsg = postResponse?.mensajeEstado ?: "Error desconocido"
                        listener?.onError(errorMsg)
                        Log.e("ARRIBO_SCREEN", "Error en respuesta: $errorMsg")
                    }
                } else {
                    val errorMsg = "Error HTTP: ${response.code()}"
                    listener?.onError(errorMsg)
                    Log.e("ARRIBO_SCREEN", errorMsg)
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                val errorMsg = "Error de conexión"
                listener?.onError(errorMsg)
                Log.e("ARRIBO_SCREEN", "Error de conexión: ${t.message}")
            }
        })
    }
}
