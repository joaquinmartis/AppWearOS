package com.android.wearable.cuandollegawearos.business

import androidx.compose.ui.graphics.Color

/***
 * Es un enumerado con las posibles presiciones de tiempo que puede haber para el colectivo. El rango es
 * @param descripcion: string con la descripcion
 * @param colorAsociado: Es el color que tiene en la UI un colectivo con esa presicion
 * @param rango: Es el tiempo maximo en segundos que puede haber entre la hora reportada y
 * la hora actual para pertenecer a esa "Presicion". Si la diferencia es menor a 60 seg, la presicion sera alta
 * si es mayor sera cualquiera de los otros
 *
 */

enum class EnumPrecision(val descripcion: String, val colorAsociado: Color, val rango: Int) {
    ALTA("Alta",Color.Green,60),
    MEDIA("Moderada", Color.Yellow,120),
    BAJA("Baja", Color.Red,200),
    ERROR("ERROR",Color.Gray,0);
}
