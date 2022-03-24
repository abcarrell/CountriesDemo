package com.acarrell.countriesdemo.repository

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCountries(): Flow<UIState>
}