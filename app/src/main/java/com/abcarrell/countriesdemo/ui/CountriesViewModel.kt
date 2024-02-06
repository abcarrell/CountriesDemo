package com.abcarrell.countriesdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.abcarrell.countriesdemo.domain.GetCountriesInteractor
import com.abcarrell.countriesdemo.domain.getCountriesInteractor
import com.abcarrell.countriesdemo.entities.Countries
import com.abcarrell.countriesdemo.entities.Country
import com.abcarrell.countriesdemo.entities.GroupListing
import com.abcarrell.countriesdemo.entities.groupListing
import com.tc.mvi.MVI
import com.tc.mvi.MVIActor
import com.tc.mvi.mvi
import kotlinx.coroutines.launch

class CountriesViewModel(
    getCountries: GetCountriesInteractor,
    private val mvi: MVIActor<UIState, Nothing, Effect>
) : ViewModel(), MVI<CountriesViewModel.UIState, Nothing, CountriesViewModel.Effect> by mvi {
    data class UIState(
        val loading: Boolean = false,
        val countries: GroupListing = emptyList()
    )

    sealed class Effect {
        data object CompleteMessage : Effect()
        data class ErrorMessage(val message: String) : Effect()
    }

    private var countriesList: Countries = listOf()

    init {
        viewModelScope.launch {
            mvi.setState { copy(loading = true) }
            getCountries().run {
                onSuccess { countries ->
                    countriesList = countries
                    mvi.setState { copy(loading = false, countries = countries.group()) }
                    mvi.setEffect { Effect.CompleteMessage }
                }
                onFailure { e ->
                    mvi.setState { copy(loading = false) }
                    mvi.setEffect {
                        Effect.ErrorMessage(e.message ?: "Error loading countries.")
                    }
                }
            }
        }
    }

    fun filterCountriesByName(value: CharSequence) {
        mvi.setState {
            copy(countries = countriesList.filter {
                it.name.startsWith(value, ignoreCase = true) || it.code.startsWith(value.take(2), ignoreCase = true)
            }.group())
        }
    }

    companion object {
        fun create() = viewModelFactory {
            initializer {
                CountriesViewModel(
                    getCountries = getCountriesInteractor(),
                    mvi = mvi(UIState())
                )
            }
        }

        private fun Countries.group(): GroupListing = asSequence()
            .sortedWith(compareBy<Country> { it.region }.thenBy { it.name })
            .groupBy { it.region }
            .groupListing()
    }
}
