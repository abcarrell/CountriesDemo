package com.acarrell.countriesdemo.repository

import android.util.Log
import com.acarrell.countriesdemo.CountriesDemo
import com.acarrell.countriesdemo.api.CountryConnection.CountryApi
import com.acarrell.countriesdemo.api.CountryConnection
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CountryRepository {
    private val countryApi: CountryApi by lazy {
        CountryConnection.retrofit.create(CountryApi::class.java)
    }

    fun getCountries(): Flow<List<Country>> = flow {
        with(countryApi.getCountries()) {
            Log.i("${CountriesDemo.APP_TAG}${this@CountryRepository::class.java.simpleName}", this.toString())
            if (isSuccessful) {
                body()?.let {
                    emit(it)
                } ?: throw IOException("No data found")
            } else {
                val err = errorBody()?.let { errorBody ->
                    gson.fromJson(errorBody.charStream(), ErrorResponse::class.java)
                } ?: ErrorResponse("No Error Response Provided")
                throw IOException("API Error code ${code()}, message ${err.message}")
            }
        }
    }.flowOn(Dispatchers.IO)

    data class ErrorResponse(@SerializedName("Message") val message: String?)

    companion object {
        private val gson = GsonBuilder().create()
    }
}