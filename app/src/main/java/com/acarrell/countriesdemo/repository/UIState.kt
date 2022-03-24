package com.acarrell.countriesdemo.repository

sealed class UIState {
    data class Response(val response: List<Country>) : UIState()
    data class Error(val message: String) : UIState()
    data class Loading(val isLoading: Boolean) : UIState()
}
