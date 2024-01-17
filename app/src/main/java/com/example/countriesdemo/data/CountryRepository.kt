package com.example.countriesdemo.data

import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.models.Countries
import java.io.IOException

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> {
        return countryApi.getCountries().run {
            if (isSuccessful) body()?.let { Result.success(it) }
                ?: Result.failure(IOException("Countries returned null"))
            else Result.failure(IOException(errorBody()?.string()))
        }
    }
}
