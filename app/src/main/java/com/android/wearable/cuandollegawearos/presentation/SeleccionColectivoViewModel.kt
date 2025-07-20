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
    data class SubLineas(val sublineas: List<SubLinea>) : SeleccionUiState()
    data class Arribos(val arribos: List<Arribo>) : SeleccionUiState()
    data class Error(val message: String) : SeleccionUiState()
    object Empty : SeleccionUiState()
}

class SeleccionColectivoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SeleccionUiState>(SeleccionUiState.Empty)
    val uiState: StateFlow<SeleccionUiState> = _uiState

    var lineaSeleccionada: LineaColectivo? = null
    var calleSeleccionada: Calle? = null
    var interseccionSeleccionada: Interseccion? = null
    var subLineaSeleccionada: SubLinea? = null

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
        cargarSubLineas(lineaSeleccionada!!, calleSeleccionada!!, interseccion)
    }

    fun cargarSubLineas(linea: LineaColectivo, calle: Calle, interseccion: Interseccion) {
        _uiState.value = SeleccionUiState.Loading
        manager.obtenerSubLineas(linea.codigo, calle.codigo, interseccion.codigo, object : SeleccionColectivoManager.Callback<List<SubLinea>> {
            override fun onSuccess(data: List<SubLinea>) {
                _uiState.value = SeleccionUiState.SubLineas(data)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
        })
    }

    fun seleccionarSubLinea(subLinea: SubLinea) {
        subLineaSeleccionada = subLinea
        cargarArribos()
    }

    fun cargarArribos() {
        _uiState.value = SeleccionUiState.Loading
        // TODO: Completar con los parámetros correctos para obtener arribos
        val managerArribos = ArribosManager()
        managerArribos.setListener(object : ArribosManager.Listener {
            override fun onArribosActualizados(arribos: List<Arribo>) {
                _uiState.value = SeleccionUiState.Arribos(arribos)
            }
            override fun onError(error: String) {
                _uiState.value = SeleccionUiState.Error(error)
            }
            override fun onLoading() {
                _uiState.value = SeleccionUiState.Loading
            }
        })
        // TODO: Cambiar por los parámetros correctos
        managerArribos.cargarArribos()
    }

    fun reiniciar() {
        lineaSeleccionada = null
        calleSeleccionada = null
        interseccionSeleccionada = null
        subLineaSeleccionada = null
        _uiState.value = SeleccionUiState.Empty
    }
} 