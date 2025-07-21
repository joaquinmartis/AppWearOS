package com.android.wearable.cuandollegawearos.presentation

import androidx.lifecycle.ViewModel
import com.android.wearable.cuandollegawearos.network.*
import com.android.wearable.cuandollegawearos.business.Arribo
import com.android.wearable.cuandollegawearos.business.ArribosManager
import com.android.wearable.cuandollegawearos.business.SeleccionColectivoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class SeleccionUiState {
    object Loading : SeleccionUiState()
    data class Lineas(val lineas: List<LineaColectivo>) : SeleccionUiState()
    data class Calles(val calles: List<Calle>) : SeleccionUiState()
    data class Intersecciones(val intersecciones: List<Interseccion>) : SeleccionUiState()
    data class Destinos(val destinos: List<Destino>) : SeleccionUiState()
    //data class Arribos(val arribos: List<Arribo>) : SeleccionUiState()
    data class Error(val message: String) : SeleccionUiState()
    object Empty : SeleccionUiState()
}

/**
 * SeleccionColectivoViewModel es una clase que es la controladora de la UI, envia mensajes hacia y desde el modelo.
 */


class SeleccionColectivoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SeleccionUiState>(SeleccionUiState.Empty)
    val uiState: StateFlow<SeleccionUiState> = _uiState

    var lineaSeleccionada: LineaColectivo? = null
    var calleSeleccionada: Calle? = null
    var interseccionSeleccionada: Interseccion? = null
    var destinoSeleccionado: Destino? = null

    private val manager = SeleccionColectivoManager()

    fun cargarLineas() {
        _uiState.value = SeleccionUiState.Loading
        manager.obtenerLineas(object : SeleccionColectivoManager.Callback<List<LineaColectivo>> {
            override fun onSuccess(data: List<LineaColectivo>) {
                _uiState.value = SeleccionUiState.Lineas(data)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
        })
    }

    fun seleccionarLinea(linea: LineaColectivo) {
        lineaSeleccionada = linea
        cargarCalles(linea)
    }

    fun cargarCalles(linea: LineaColectivo) {
        _uiState.value = SeleccionUiState.Loading
        manager.obtenerCalles(linea.codigo, object : SeleccionColectivoManager.Callback<List<Calle>> {
            override fun onSuccess(data: List<Calle>) {
                _uiState.value = SeleccionUiState.Calles(data)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
        })
    }

    fun seleccionarCalle(calle: Calle) {
        calleSeleccionada = calle
        cargarIntersecciones(lineaSeleccionada!!, calle)
    }

    fun cargarIntersecciones(linea: LineaColectivo, calle: Calle) {
        _uiState.value = SeleccionUiState.Loading
        manager.obtenerIntersecciones(linea.codigo, calle.codigo, object : SeleccionColectivoManager.Callback<List<Interseccion>> {
            override fun onSuccess(data: List<Interseccion>) {
                _uiState.value = SeleccionUiState.Intersecciones(data)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
        })
    }

    fun seleccionarInterseccion(interseccion: Interseccion) {
        interseccionSeleccionada = interseccion
        cargarDestinos(lineaSeleccionada!!, calleSeleccionada!!, interseccion)
    }

    fun cargarDestinos(linea: LineaColectivo, calle: Calle, interseccion: Interseccion) {
        _uiState.value = SeleccionUiState.Loading
        manager.obtenerDestinos(linea.codigo, calle.codigo, interseccion.codigo, object : SeleccionColectivoManager.Callback<List<Destino>> {
            override fun onSuccess(data: List<Destino>) {
                _uiState.value = SeleccionUiState.Destinos(data)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
        })
    }

    fun seleccionarDestino(destino: Destino) {
        destinoSeleccionado = destino
    }


    fun reiniciar() {
        lineaSeleccionada = null
        calleSeleccionada = null
        interseccionSeleccionada = null
        destinoSeleccionado = null
        _uiState.value = SeleccionUiState.Empty
    }
}
