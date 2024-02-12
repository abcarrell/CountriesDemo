package com.tc.countries.domain

import com.tc.countries.data.CountryApi
import com.tc.countries.data.ResponseDataMapper
import com.tc.countries.entities.Countries

interface CountryRepository {
    suspend fun getCountries(): Result<Countries>
}

class CountryRepositoryImpl(
    private val countryApi: CountryApi,
    private val responseDataMapper: ResponseDataMapper<Countries>
) : CountryRepository {
    override suspend fun getCountries(): Result<Countries> =
        countryApi.getCountries().run(responseDataMapper)
}
