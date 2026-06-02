package com.example.cineversemovieapp.network

import com.example.cineversemovieapp.network.Auth.AuthApi
import com.example.cineversemovieapp.network.Auth.AuthInterceptor
import com.example.cineversemovieapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // token holder — updated after login
    var token: String = ""

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor { token })  // 👈 adds JWT automatically
            .build()
    }

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}