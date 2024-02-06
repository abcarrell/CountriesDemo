package com.tc.mvi

interface MVIActor<UiState, UiAction, SideEffect> : MVI<UiState, UiAction, SideEffect> {
    fun setState(reduce: UiState.() -> UiState)

    fun setState(state: UiState)

    fun setEffect(effect: SideEffect)

    fun setEffect(effect: () -> SideEffect)
}
