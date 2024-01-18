package com.example.countriesdemo.usecases

import com.example.countriesdemo.Interactor
import com.example.countriesdemo.api.CountryApi
import com.example.countriesdemo.entities.entities.Countries
import com.example.countriesdemo.repository.CountryRepository
import com.example.countriesdemo.repository.CountryRepositoryImpl

fun interface GetCountriesInteractor : Interactor<Result<Countries>>

fun getCountriesInteractor(repository: CountryRepository = CountryRepositoryImpl(CountryApi.instance)) =
    GetCountriesInteractor(repository::getCountries)
