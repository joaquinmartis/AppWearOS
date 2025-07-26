package com.android.wearable.cuandollegawearos.business

import android.util.Log
import com.android.wearable.cuandollegawearos.network.PostResponse
import com.android.wearable.cuandollegawearos.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.*


/**
 * ArribosManager es una clase que juega de modelo en el MVC. Maneja la logica de las request HTTP que se crean
 * y el manejo de errores.
 *
 */

class ArribosManager (
    private val seleccionRepository: SeleccionRepository = SeleccionRepository
){
    interface Listener {
        fun onArribosActualizados(arribos: List<Arribo>)
        fun onError(error: String)
        fun onLoading()
    }

    private var listener: Listener? = null
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Guardar arribos originales y el instante de actualización
    private var arribosOriginales: List<Arribo> = emptyList()
    private var lastUpdate: Long = 0L

    private lateinit var paradaActual: String
    private lateinit var lineaActual: String

    fun setListener(listener: Listener) {
        this.listener = listener
    }


    /**
     * Esta funcion se encarga de hacer la solicitud a la api de los arribos de colectivos en una parada y una determinada
     * linea de colectivo. Actualiza la UI dinamicamente, por eso tiene listeners,
     */
    fun cargarArribos() {
        val destino = seleccionRepository.getDestino()
        val linea = seleccionRepository.getLinea()
        if (destino == null || linea == null) {
            listener?.onError("Faltan datos de selección")
            return
        }
        paradaActual = destino.identificador
        lineaActual = linea.codigo
        cargarArribosAPI()
    }

    fun actualizarArribos() {
        cargarArribos()
    }

    private fun cargarArribosAPI(){
        listener?.onLoading()
        timerJob?.cancel()


        val call = RetrofitClient.apiService.sendPost(
            accion = EnumAcciones.ACCION_ARRIBOS.nombreAccion,
            identificadorParada = paradaActual,
            codigoLineaParada = lineaActual
        )
        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    val postResponse = response.body()
                    if (postResponse != null && postResponse.codigoEstado == 0) {
                        val arribos = postResponse.arribos?.map { Arribo.fromApi(it) } ?: emptyList()
                        arribosOriginales = arribos
                        lastUpdate = System.currentTimeMillis()
                        notificarArribosActualizados()
                        iniciarTemporizador()
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


    private fun iniciarTemporizador() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive) {
                delay(60_000) // 1 minuto
                notificarArribosActualizados()
            }
        }
    }

    private fun notificarArribosActualizados() {
        val elapsedMinutes = ((System.currentTimeMillis() - lastUpdate) / 60000).toInt()
        val arribosActualizados = arribosOriginales.map { arribo ->
            val minutosOriginal = arribo.arribo.trim().split(" ")[0].toIntOrNull()
            val nuevoTexto = if (minutosOriginal != null) {
                val minutosRestantes = minutosOriginal - elapsedMinutes
                if (minutosRestantes > 0) "${minutosRestantes} min" else "Llegando"
            } else arribo.arribo
            arribo.copy(arribo = nuevoTexto)
        }
        listener?.onArribosActualizados(arribosActualizados)
    }

    fun limpiar() {
        timerJob?.cancel()
    }
}
