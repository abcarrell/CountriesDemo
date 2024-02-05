package com.abcarrell.countriesdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.abcarrell.countriesdemo.domain.GetCountriesInteractor
import com.abcarrell.countriesdemo.domain.getCountriesInteractor
import com.abcarrell.countriesdemo.entities.Countries
import com.abcarrell.countriesdemo.entities.GroupItem
import com.abcarrell.countriesdemo.entities.GroupListing
import com.abcarrell.countriesdemo.entities.groupListing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CountriesViewModel(getCountries: GetCountriesInteractor) : ViewModel() {
    data class UIState(
        val loading: Boolean = false,
        val countries: GroupListing = emptyList()
    )

    sealed class Effect {
        data object CompleteMessage : Effect()
        data class ErrorMessage(val message: String) : Effect()
    }

    //region mvi_operations
    // Under normal conditions, if this were to be reused, we would create a subclass of ViewModel holding these
    // operations and inherit our view models from that class to reduce boilerplate

    private val _state: MutableStateFlow<UIState> by lazy { MutableStateFlow(UIState()) }
    val state = _state.asStateFlow()

    private fun setState(reduce: UIState.() -> UIState) {
        _state.value = state.value.reduce()
    }

    // Using Channel.receiveAsFlow as the effects flow does not require
    // any buffering capability as from a shared flow
    private val _effects: Channel<Effect> by lazy { Channel() }
    val effects = _effects.receiveAsFlow()

    private fun setEffect(effect: () -> Effect) {
        _effects.trySend(effect())
    }
    //endregion

    private var countriesList: Countries = listOf()

    init {
        viewModelScope.launch {
            setState { copy(loading = true) }
            getCountries().run {
                onSuccess { countries ->
                    countriesList = countries
                    setState { copy(loading = false, countries = countries.group()) }
                    setEffect { Effect.CompleteMessage }
                }
                onFailure { e ->
                    setState { copy(loading = false) }
                    setEffect {
                        Effect.ErrorMessage(e.message ?: "Error loading countries.")
                    }
                }
            }
        }
    }

    fun filterCountriesByName(value: CharSequence) {
        setState {
            copy(countries = countriesList.filter {
                it.name.startsWith(value, ignoreCase = true)
            }.group())
        }
    }

    companion object {
        fun create() = viewModelFactory {
            initializer {
                CountriesViewModel(getCountriesInteractor())
            }
        }

        private fun Countries.group(): GroupListing = asSequence()
            .sortedBy { it.name }
            .groupBy { it.name.first().toString() }
            .groupListing()
    }
}
