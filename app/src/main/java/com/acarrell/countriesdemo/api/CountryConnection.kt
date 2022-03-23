package com.acarrell.countriesdemo.api

import com.acarrell.countriesdemo.repository.Country
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object CountryConnection {
    val retrofit: Retrofit
        get() {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build().let { client ->
                    Retrofit.Builder()
                        .baseUrl("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build()
                }
        }

    interface CountryApi {
        @GET("countries.json")
        suspend fun getCountries(): Response<List<Country>>
    }
}