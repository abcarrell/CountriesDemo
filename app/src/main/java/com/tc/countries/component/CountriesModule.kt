package com.tc.countries.component

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tc.countries.data.CountryApi
import com.tc.countries.data.ResponseDataMapper
import com.tc.countries.domain.CountryRepository
import com.tc.countries.domain.CountryRepositoryImpl
import com.tc.countries.domain.GetCountriesUseCase
import com.tc.countries.entities.Countries
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object CountriesModule {
    private const val BASE_URL =
        "https://gist.githubusercontent.com/peymano-wmt/" +
                "32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/"

    fun provideGson(): Gson = GsonBuilder().create()

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    fun provideCountryApi(gson: Gson, okHttpClient: OkHttpClient): CountryApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(CountryApi::class.java)

    fun provideResponseDataMapper(): ResponseDataMapper<Countries> = ResponseDataMapper { response ->
        runCatching {
            if (response.isSuccessful) checkNotNull(response.body()) { "Response is null" }
            else throw with(response) {
                IOException("API Error ${code()}: ${errorBody()?.string() ?: message()}")
            }
        }
    }

    fun provideCountryRepository(
        service: CountryApi,
        responseDataMapper: ResponseDataMapper<Countries>
    ): CountryRepository = CountryRepositoryImpl(service, responseDataMapper)

    fun provideGetCountriesUseCase(countryRepository: CountryRepository): GetCountriesUseCase =
        GetCountriesUseCase(countryRepository::getCountries)
}
