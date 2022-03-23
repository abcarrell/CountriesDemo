package com.acarrell.countriesdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.acarrell.countriesdemo.repository.Country
import com.acarrell.countriesdemo.repository.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class MainViewModel : ViewModel() {
    private val repository: CountryRepository by lazy {
        CountryRepository()
    }

    val events = MutableStateFlow<ViewEvent>(ViewEvent.Started)

    val countryData: LiveData<List<Country>> by lazy {
        repository.getCountries()
            .onStart {
                events.value = ViewEvent.Started
            }
            .catch {
                events.value = ViewEvent.Error(it.message ?: "Error getting data.")
            }
            .asLiveData()
    }

    sealed class ViewEvent {
        object Started : ViewEvent()
        object Complete : ViewEvent()
        data class Error(val message: String) : ViewEvent()
    }
}