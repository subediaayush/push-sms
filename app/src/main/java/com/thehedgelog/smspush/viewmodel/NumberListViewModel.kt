package com.thehedgelog.smspush.viewmodel

import androidx.lifecycle.ViewModel
import com.thehedgelog.smspush.ui.state.NumberListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NumberListViewModel() : UIStateViewModel<NumberListState>(NumberListState())