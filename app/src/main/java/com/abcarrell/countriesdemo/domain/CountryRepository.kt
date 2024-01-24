package com.abcarrell.countriesdemo.domain

import com.abcarrell.countriesdemo.api.CountryApi
import com.abcarrell.countriesdemo.entities.Countries
import java.io.IOException

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> {
        return runCatching {
            countryApi.getCountries().run {
                if (isSuccessful) body() ?: listOf()
                else throw IOException(errorBody()?.string())
            }
        }
    }
}
