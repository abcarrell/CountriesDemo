package com.acarrell.countriesdemo.api

import com.acarrell.countriesdemo.repository.Country
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountryApi {
    @GET(ENDPOINT)
    suspend fun getCountries(): Response<List<Country>>

    companion object {
        private const val BASE_URL = "https://gist.githubusercontent.com/"
        private const val ENDPOINT =
            "peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json"

        fun initialize(): CountryApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(CountryApi::class.java)
        }
    }
}