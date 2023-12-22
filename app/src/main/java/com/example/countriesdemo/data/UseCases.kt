package com.example.countriesdemo.data

import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.models.Country

fun interface GetCountriesUseCase : suspend () -> Result<List<Country>>

object UseCases {
    fun getCountries(repository: CountryRepository = CountryRepositoryImpl(CountryApi.create())) =
        GetCountriesUseCase(repository::getCountries)
}
