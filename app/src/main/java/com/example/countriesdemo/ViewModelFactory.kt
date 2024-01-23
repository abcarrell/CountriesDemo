package com.example.countriesdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

internal fun withFactory(create: () -> ViewModel) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create() as T
    }
}
