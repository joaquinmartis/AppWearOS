package com.android.wearable.cuandollegawearos.business

class ApiResponseException(
    val code: Int,
    val apiMessage: String
) : IllegalStateException("API error (code=$code, message=$apiMessage)")
