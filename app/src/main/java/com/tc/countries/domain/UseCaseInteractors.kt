package com.tc.countries.domain

import com.tc.countries.Interactor
import com.tc.countries.data.CountryApi
import com.tc.countries.data.responseDataMapper
import com.tc.countries.entities.Countries

// See Denis Brandi's article on using SAM interface for use case interactors:
// https://betterprogramming.pub/how-to-avoid-use-cases-boilerplate-in-android-d0c9aa27ef27
fun interface GetCountriesInteractor : Interactor<Result<Countries>>

fun getCountriesInteractor(
    repository: CountryRepository = CountryRepositoryImpl(
        CountryApi.service,
        responseDataMapper()
    )
) = GetCountriesInteractor(repository::getCountries)
