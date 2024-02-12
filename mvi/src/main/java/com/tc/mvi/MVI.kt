package com.tc.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MVI<UiState, UiAction, SideEffect> {
    val state: StateFlow<UiState>
    val effects: Flow<SideEffect>

    fun onAction(uiAction: UiAction)
}
