package com.android.wearable.cuandollegawearos.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers

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
    ): Call<PostResponse>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerLineas(
        @Field("accion") accion: String // Ejemplo: "RecuperarLineas"
    ): Call<LineasResponse>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerCalles(
        @Field("accion") accion: String, // Ejemplo: "RecuperarCalles"
        @Field("codLinea") codigoLinea: String
    ): Call<CallesResponse>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerIntersecciones(
        @Field("accion") accion: String, // Ejemplo: "RecuperarIntersecciones"
        @Field("codLinea") codigoLinea: String,
        @Field("codCalle") codigoCalle: String
    ): Call<InterseccionesResponse>

    @FormUrlEncoded
    @Headers(
        "Origin: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php",
        "Referer: https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php"
    )
    @POST("webWS.php")
    fun obtenerDestinos(
        @Field("accion") accion: String, // Ejemplo: "RecuperarDestinos"
        @Field("codLinea") codigoLinea: String,
        @Field("codCalle") codigoCalle: String,
        @Field("codInterseccion") codigoInterseccion: String
    ): Call<DestinosResponse>
}
