package com.android.wearable.cuandollegawearos.business

enum class EnumAcciones (val nombreAccion: String) {
    ACCION_LINEAS("RecuperarLineaPorCuandoLlega"),
    ACCION_CALLES("RecuperarCallesPrincipalPorLinea"),
    ACCION_INTERSECCIONES("RecuperarInterseccionPorLineaYCalle"),
    ACCION_DESTINOS("RecuperarParadasConBanderaPorLineaCalleEInterseccion"),
    ERROR("ERROR");
}
