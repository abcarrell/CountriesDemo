package com.tc.countries

import com.tc.countries.domain.GetCountriesUseCase
import com.tc.countries.entities.Country
import com.tc.countries.ui.CountriesViewModel
import com.tc.mvi.MVIActor
import com.tc.mvi.mvi
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

    private lateinit var getCountriesUseCase: GetCountriesUseCase

    private lateinit var mvi: MVIActor<CountriesViewModel.UIState, Nothing, CountriesViewModel.Effect>

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

        getCountriesUseCase = GetCountriesUseCase {
            delay(2_000)
            Result.success(listOf(country))
        }

        mvi = mvi(CountriesViewModel.UIState())

        runTest {
            viewModel = CountriesViewModel(getCountriesUseCase, mvi)

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
