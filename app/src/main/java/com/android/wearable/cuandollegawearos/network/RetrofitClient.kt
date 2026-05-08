package com.android.wearable.cuandollegawearos.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * RetrofitClient es una clase que solo tiene la funcion de ser cliente HTTP. Todas las llamadas HTTP pasan por aca
 */


object RetrofitClient {
    private const val BASE_URL = "https://appsl.mardelplata.gob.ar/app_cuando_llega/"

    private val client = OkHttpClient.Builder()
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
