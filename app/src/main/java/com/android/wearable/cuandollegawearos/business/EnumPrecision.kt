package com.android.wearable.cuandollegawearos.business

import androidx.compose.ui.graphics.Color

enum class EnumPrecision(val descripcion: String, val colorAsociado: Color, val rango: Int) {
    ALTA("Alta",Color.Green,60),
    MEDIA("Moderada", Color.Yellow,120),
    BAJA("Baja", Color.Red,200),
    ERROR("ERROR",Color.Gray,0);
}
