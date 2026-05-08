package com.android.wearable.cuandollegawearos.business


/***
 * Este enumerado muestra todas las acciones posibles para cada Accion en la API
 * TODO: Agregar tambien parametros especificos para cada una y dejar todo asentado en este enum
 *
 */

enum class EnumAcciones (val nombreAccion: String) {
    ACCION_LINEAS("RecuperarLineaPorCuandoLlega"),
    ACCION_CALLES("RecuperarCallesPrincipalPorLinea"),
    ACCION_INTERSECCIONES("RecuperarInterseccionPorLineaYCalle"),
    ACCION_DESTINOS("RecuperarParadasConBanderaPorLineaCalleEInterseccion"),
    ACCION_ARRIBOS("RecuperarProximosArribosW"),
    ERROR("ERROR");
}
