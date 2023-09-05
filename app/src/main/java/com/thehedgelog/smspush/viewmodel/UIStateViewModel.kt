package com.thehedgelog.smspush.viewmodel

import androidx.lifecycle.ViewModel
import com.thehedgelog.smspush.SmsPreferences
import com.thehedgelog.smspush.ui.state.NumberListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class UIStateViewModel<T>(initialState: T) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    var uiState: StateFlow<T> = _uiState.asStateFlow()
    val updateState = { newState: T -> _uiState.value = newState }

    fun update(newState: T) {
        updateState(newState)
    }


}