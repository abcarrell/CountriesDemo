package com.tc.countries.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tc.countries.domain.GetCountriesUseCase
import com.tc.countries.domain.getCountriesUseCase
import com.tc.countries.entities.Countries
import com.tc.mvi.MVI
import com.tc.mvi.MVIActor
import com.tc.mvi.mvi
import kotlinx.coroutines.launch

class CountriesViewModel(
    getCountries: GetCountriesUseCase,
    private val mvi: MVIActor<UIState, Nothing, Effect>
) : ViewModel(), MVI<CountriesViewModel.UIState, Nothing, CountriesViewModel.Effect> by mvi {
    data class UIState(
        val loading: Boolean = false,
        val countries: Countries = emptyList()
    )

    sealed interface Effect {
        data object CompleteMessage : Effect
        data class ErrorMessage(val message: String) : Effect
    }

    private var countriesList: Countries = listOf()

    init {
        viewModelScope.launch {
            mvi.setState { copy(loading = true) }
            getCountries().run {
                onSuccess { countries ->
                    countriesList = countries
                    mvi.setState { copy(loading = false, countries = countries) }
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

    companion object {
        fun create() = viewModelFactory {
            initializer {
                CountriesViewModel(
                    getCountries = getCountriesUseCase(),
                    mvi = mvi(UIState())
                )
            }
        }
    }
}
