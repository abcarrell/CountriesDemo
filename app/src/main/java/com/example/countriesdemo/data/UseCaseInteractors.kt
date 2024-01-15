package com.example.countriesdemo.data

import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.models.Country

fun interface Interactor<T> : suspend () -> T
fun interface GetCountriesInteractor : Interactor<Result<List<Country>>>

fun getCountriesInteractor(repository: CountryRepository = CountryRepositoryImpl(CountryApi.instance)) =
    GetCountriesInteractor(repository::getCountries)
