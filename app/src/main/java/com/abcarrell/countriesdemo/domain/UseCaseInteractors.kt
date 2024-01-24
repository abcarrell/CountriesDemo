package com.abcarrell.countriesdemo.domain

import com.abcarrell.countriesdemo.Interactor
import com.abcarrell.countriesdemo.api.CountryApi
import com.abcarrell.countriesdemo.entities.Countries

// See Denis Brandi's article on using SAM interface for use case interactors:
// https://betterprogramming.pub/how-to-avoid-use-cases-boilerplate-in-android-d0c9aa27ef27
fun interface GetCountriesInteractor : Interactor<Result<Countries>>

fun getCountriesInteractor(repository: CountryRepository = CountryRepositoryImpl(CountryApi.service)) =
    GetCountriesInteractor(repository::getCountries)
