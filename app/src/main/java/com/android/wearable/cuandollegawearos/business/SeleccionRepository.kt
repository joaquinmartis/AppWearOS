package com.android.wearable.cuandollegawearos.business

object SeleccionRepository {
    private var destinoSeleccionado: Destino? = null
    private var lineaSeleccionada: LineaColectivo? = null
    private var calleSeleccionada: Calle? = null
    private var interseccionSeleccionada: Interseccion? = null

    fun setDestino(destino: Destino?) {
        destinoSeleccionado = destino
    }

    fun setLinea(linea: LineaColectivo?) {
        lineaSeleccionada = linea
    }

    fun setCalle(calle: Calle?) {
        calleSeleccionada = calle
    }

    fun setInterseccion(interseccion: Interseccion?) {
        interseccionSeleccionada = interseccion
    }

    fun getDestino(): Destino? = destinoSeleccionado
    fun getLinea(): LineaColectivo? = lineaSeleccionada
    fun getCalle(): Calle? = calleSeleccionada
    fun getInterseccion(): Interseccion? = interseccionSeleccionada
}
