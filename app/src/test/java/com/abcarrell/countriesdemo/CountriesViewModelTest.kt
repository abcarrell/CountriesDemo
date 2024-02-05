package com.abcarrell.countriesdemo

import com.abcarrell.countriesdemo.domain.GetCountriesInteractor
import com.abcarrell.countriesdemo.entities.Country
import com.abcarrell.countriesdemo.ui.CountriesViewModel
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountriesViewModelTest {
    private lateinit var viewModel: CountriesViewModel

    private lateinit var getCountriesInteractor: GetCountriesInteractor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `creating viewModel object returns countriesList`() {
        val country = Country(
            name = "United States of America",
            code = "US",
            region = "NA",
            capital = "Washington, DC",
            flag = "",
            currency = mockk(),
            language = mockk()
        )

        getCountriesInteractor = GetCountriesInteractor {
            delay(2_000)
            Result.success(listOf(country))
        }

        runTest {
            viewModel = CountriesViewModel(getCountriesInteractor)

            with(viewModel.state.value) {
                assertTrue(loading)
                assertTrue(countries.isEmpty())
            }

            delay(2_000)

            with(viewModel.state.value) {
                assertFalse(loading)
                with(countries.first { it.data is String }.data as String) {
                    assertEquals("U", this)
                }
                with(countries.first { it.data is Country }.data as Country) {
                    assertEquals("US", code)
                    assertEquals("NA", region)
                }
            }
        }
    }
}
