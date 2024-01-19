package com.example.countriesdemo.usecases

import com.example.countriesdemo.Interactor
import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.entities.Countries
import com.example.countriesdemo.repository.CountryRepository
import com.example.countriesdemo.repository.CountryRepositoryImpl

// See Denis Brandi's article on using SAM interface for use case interactors:
// https://betterprogramming.pub/how-to-avoid-use-cases-boilerplate-in-android-d0c9aa27ef27
fun interface GetCountriesInteractor : Interactor<Result<Countries>>

fun getCountriesInteractor(repository: CountryRepository = CountryRepositoryImpl(CountryApi.service)) =
    GetCountriesInteractor(repository::getCountries)
