package com.android.wearable.cuandollegawearos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.wearable.cuandollegawearos.business.Arribo
import com.android.wearable.cuandollegawearos.business.ArribosManager
import com.android.wearable.cuandollegawearos.business.SeleccionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ArribosViewModel es una clase que es la controladora de la UI, envia mensajes hacia y desde el modelo.
 */

sealed class ArribosUiState {
    object Loading : ArribosUiState()
    data class Success(val arribos: List<Arribo>) : ArribosUiState()
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
        _uiState.value = ArribosUiState.Success(arribos)
    }

    override fun onError(error: String) {
        _uiState.value = ArribosUiState.Error(error)
    }

    override fun onLoading() {
        _uiState.value = ArribosUiState.Loading
    }
} 