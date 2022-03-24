package com.acarrell.countriesdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.acarrell.countriesdemo.repository.Repository
import com.acarrell.countriesdemo.repository.UIState

class MainViewModel(private val repository: Repository) : ViewModel() {

    val countryData: LiveData<UIState> by lazy {
        repository.getCountries().asLiveData()
    }
}