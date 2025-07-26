package com.android.wearable.cuandollegawearos.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers


/**
 * ApiService es una mega clase que engloba todas las posibles acciones que se pueden realizar con la API. Incluye el bypass del CORS.
 *
 *
 */

interface ApiService {
    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun sendPost(
        @Field("accion") accion: String,
        @Field("identificadorParada") identificadorParada: String,
        @Field("codigoLineaParada") codigoLineaParada: String
    ): Call<ArribosResponseAPI>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerLineas(
        @Field("accion") accion: String
    ): Call<LineasResponseAPI>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerCalles(
        @Field("accion") accion: String,
        @Field("codLinea") codigoLinea: String
    ): Call<CallesResponseAPI>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerIntersecciones(
        @Field("accion") accion: String,
        @Field("codLinea") codigoLinea: String,
        @Field("codCalle") codigoCalle: String
    ): Call<InterseccionesResponseAPI>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerDestinos(
        @Field("accion") accion: String,
        @Field("codLinea") codigoLinea: String,
        @Field("codCalle") codigoCalle: String,
        @Field("codInterseccion") codigoInterseccion: String
    ): Call<DestinosResponseAPI>
}
