package com.example.countriesdemo.repository

import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.entities.Countries
import java.io.IOException

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> {
        return runCatching {
            countryApi.getCountries().run {
                if (isSuccessful) body() ?: throw IOException("Countries returned null")
                else throw IOException(errorBody()?.string())
            }
        }
    }
}
