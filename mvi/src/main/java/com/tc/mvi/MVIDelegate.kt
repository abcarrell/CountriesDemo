package com.tc.mvi

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class MVIDelegate<UiState, UiAction, SideEffect> internal constructor(
    initialUiState: UiState
) : MVIActor<UiState, UiAction, SideEffect> {
    private val _uiState = MutableStateFlow(initialUiState)
    override val state: StateFlow<UiState> = _uiState.asStateFlow()

    private val _sideEffect: Channel<SideEffect> by lazy {
        Channel(capacity = Channel.UNLIMITED)
    }
    override val effects: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    override fun onAction(uiAction: UiAction) {}

    override fun setState(reduce: UiState.() -> UiState) {
        _uiState.update(reduce)
    }

    override fun setState(state: UiState) {
        _uiState.update { state }
    }

    override fun setEffect(effect: SideEffect) {
        _sideEffect.trySend(effect)
    }

    override fun setEffect(effect: () -> SideEffect) {
        _sideEffect.trySend(effect())
    }
}

fun <UiState, UiAction, SideEffect> mvi(initialUiState: UiState) =
    MVIDelegate<UiState, UiAction, SideEffect>(initialUiState)
