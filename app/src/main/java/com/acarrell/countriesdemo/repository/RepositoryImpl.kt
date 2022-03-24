package com.acarrell.countriesdemo.repository

import android.util.Log
import com.acarrell.countriesdemo.CountriesDemo
import com.acarrell.countriesdemo.api.CountryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RepositoryImpl(private val countryApi: CountryApi) : Repository {

    override fun getCountries(): Flow<UIState> = flow {
        with(countryApi.getCountries()) {
            Log.i(CountriesDemo.logTag(this@RepositoryImpl), this.toString())
            if (isSuccessful) {
                body()?.let {
                    emit(UIState.Response(it))
                } ?: emit(UIState.Error(message()))
            } else {
                emit(UIState.Error(message()))
            }
        }
    }.flowOn(Dispatchers.IO)
}