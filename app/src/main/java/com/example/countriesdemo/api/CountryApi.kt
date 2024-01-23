package com.example.countriesdemo.api

import android.util.Log
import com.example.countriesdemo.entities.Countries
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface CountryApi {
    @GET("countries.json")
    suspend fun getCountries(): Response<Countries>

    companion object {
        private const val BASE_URL =
            "https://gist.githubusercontent.com/peymano-wmt/" +
                    "32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/"

        val service: CountryApi by lazy {
            val gson = GsonBuilder().create()

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(
                    HttpLoggingInterceptor { msg ->
                        Log.v("CountryApi", msg)
                    }.setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(CountryApi::class.java)
        }
    }
}
