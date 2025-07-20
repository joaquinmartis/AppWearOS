package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.PostResponse
import com.android.wearable.cuandollegawearos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun cargarArribos(idParada: String = "P4002", codLineaParada: String = "121") {
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
