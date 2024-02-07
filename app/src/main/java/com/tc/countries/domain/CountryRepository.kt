package com.tc.countries.domain

import com.tc.countries.data.CountryApi
import com.tc.countries.data.mapToResult
import com.tc.countries.entities.Countries

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(private val countryApi: CountryApi) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> =
        countryApi.getCountries().mapToResult()
}
