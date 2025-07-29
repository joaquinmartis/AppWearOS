package com.android.wearable.cuandollegawearos.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.wearable.cuandollegawearos.business.Arribo
import com.android.wearable.cuandollegawearos.business.ArribosManager
import com.android.wearable.cuandollegawearos.business.LineaColectivo
import com.android.wearable.cuandollegawearos.business.SeleccionRepository
import com.android.wearable.cuandollegawearos.network.LineaColectivoAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ArribosViewModel es una clase que es la controladora de la UI, envia mensajes hacia y desde el modelo.
 */

sealed class ArribosUiState {
    object Loading : ArribosUiState()
    data class Success(val arribos: List<ArriboUI>) : ArribosUiState()
    data class Error(val message: String) : ArribosUiState()
}

class ArribosViewModel : ViewModel(), ArribosManager.Listener {
    private val manager = ArribosManager()
    private val _uiState = MutableStateFlow<ArribosUiState>(ArribosUiState.Loading)
    val uiState: StateFlow<ArribosUiState> = _uiState

    init {
        manager.setListener(this)
    }

    fun cargarArribos() {
        manager.cargarArribos()
    }

    fun actualizarArribos() {
        manager.actualizarArribos()
    }

    override fun onArribosActualizados(arribos: List<Arribo>) {

        _uiState.value = ArribosUiState.Success(esteticaArribos(arribos))
    }

    override fun onError(error: String) {
        _uiState.value = ArribosUiState.Error(error)
    }

    override fun onLoading() {
        _uiState.value = ArribosUiState.Loading
    }

    fun Arribo.toDomain() = ArriboUI(
        linea = descripcionLinea,
        sentido = descripcionBandera,
        tiempo = if (arribo.contains("arribando", ignoreCase = true)) {
            "Llegando"
        } else {
            (extraerNumero(arribo)?.toString() ?: "?") + " min."
        },
        precision = precision.descripcion,
        color = precision.colorAsociado
    )

    fun extraerNumero(entrada: String): Int? {
        val regex = Regex("""\d+""")
        return regex.find(entrada)?.value?.toIntOrNull()
    }

    fun esteticaArribos(arribos: List<Arribo>): List<ArriboUI> {
        return arribos.map { it.toDomain() }
    }



}
