package com.android.wearable.cuandollegawearos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.wearable.cuandollegawearos.business.Arribo
import com.android.wearable.cuandollegawearos.business.ArribosManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        cargarArribos()
    }

    fun cargarArribos(idParada: String = "P4002", codLineaParada: String = "121") {
        manager.cargarArribos(idParada, codLineaParada)
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