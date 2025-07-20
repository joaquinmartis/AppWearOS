package com.android.wearable.cuandollegawearos.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://appsl.mardelplata.gob.ar/app_cuando_llega/" // Cambia por tu URL base

    // Interceptor para agregar headers personalizados (CORS, etc.)
    /*private val headerInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Origin", "https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php") // Cambia por el valor que necesites
            .header("Referer", "https://appsl.mardelplata.gob.ar/app_cuando_llega/web/cuando.php") // Cambia por el valor que necesites
        val request = requestBuilder.build()
        chain.proceed(request)
    }*/

    private val client = OkHttpClient.Builder()
        //.addInterceptor(headerInterceptor)
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
