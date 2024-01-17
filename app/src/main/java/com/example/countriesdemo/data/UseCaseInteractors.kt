package com.example.countriesdemo.data

import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.models.Countries

fun interface Interactor<T> : suspend () -> T
fun interface GetCountriesInteractor : Interactor<Result<Countries>>

fun getCountriesInteractor(repository: CountryRepository = CountryRepositoryImpl(CountryApi.instance)) =
    GetCountriesInteractor(repository::getCountries)
