package com.thehedgelog.smspush.viewmodel

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.thehedgelog.smspush.SmsApplication
import com.thehedgelog.smspush.SmsPreferences.Companion.KEY_BROADCASTING_TOGGLE
import com.thehedgelog.smspush.service.SmsReceiverService
import com.thehedgelog.smspush.ui.state.BroadcastState

class BroadcastViewModel : UIStateViewModel<BroadcastState>(BroadcastState(true)) {

    private val preferences = SmsApplication.instance.preferences
    private var broadcastStatusLiveData = preferences[KEY_BROADCASTING_TOGGLE].asLiveData()
        .map { uiState.value.copy(isBroadcasting = it ?: false) }
    private val broadcastStatusObserver =
        Observer<BroadcastState> { this@BroadcastViewModel.updateState(it) }

    init {
        broadcastStatusLiveData.observeForever(broadcastStatusObserver)
    }

    override fun onCleared() {
        super.onCleared()
        broadcastStatusLiveData.removeObserver(broadcastStatusObserver)
    }

    val toggleBroadcast: (Context, Boolean) -> Unit = { context, broadcast ->
        preferences[KEY_BROADCASTING_TOGGLE] = broadcast
        if (broadcast) SmsReceiverService.startService(context)
        else SmsReceiverService.stopService(context)
    }

}
