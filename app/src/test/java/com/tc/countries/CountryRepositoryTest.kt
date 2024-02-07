package com.tc.countries

import com.tc.countries.data.CountryApi
import com.tc.countries.entities.Countries
import com.tc.countries.entities.Country
import com.tc.countries.domain.CountryRepository
import com.tc.countries.domain.CountryRepositoryImpl
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class CountryRepositoryTest {
    private lateinit var countryRepository: CountryRepository

    private lateinit var countryApi: CountryApi

    @Test
    fun `getCountries returns a Country response`() {
        val countries: Countries = listOf(
            Country(
                name = "United States of America",
                code = "US",
                region = "NA",
                capital = "Washington, DC",
                flag = "",
                currency = mockk(),
                language = mockk()
            ),
            Country(
                name = "United Kingdom",
                code = "UK",
                region = "EU",
                capital = "London",
                flag = "",
                currency = mockk(),
                language = mockk()
            ),
            Country(
                name = "Japan",
                code = "JP",
                region = "AS",
                capital = "Tokyo",
                flag = "",
                currency = mockk(),
                language = mockk()
            )
        )
        countryApi = object : CountryApi {
            override suspend fun getCountries(): Response<Countries> {
                delay(2_000)
                return Response.success(countries)
            }
        }

        countryRepository = CountryRepositoryImpl(countryApi)

        runTest {
            with(countryRepository.getCountries()) {
                assertTrue(isSuccess)
                assertNotNull(getOrNull())
                getOrNull()?.let { countries ->
                    assertEquals(3, countries.size)
                    assertEquals("US", countries.first().code)
                    assertEquals("United Kingdom", countries[1].name)
                    assertEquals("Tokyo", countries.last().capital)
                }
            }
        }
    }
}
