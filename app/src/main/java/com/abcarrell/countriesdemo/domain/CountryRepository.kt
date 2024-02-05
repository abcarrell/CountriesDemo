package com.abcarrell.countriesdemo.domain

import com.abcarrell.countriesdemo.data.CountryApi
import com.abcarrell.countriesdemo.data.mapToResult
import com.abcarrell.countriesdemo.entities.Countries

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> =
        countryApi.getCountries().mapToResult()
}
