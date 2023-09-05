package com.thehedgelog.smspush.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.thehedgelog.smspush.SmsApplication
import com.thehedgelog.smspush.SmsApplication.Companion.TAG
import com.thehedgelog.smspush.firebase.firestore.Firestore
import com.thehedgelog.smspush.model.LocalSmsMessage
import com.thehedgelog.smspush.model.smsValue
import com.thehedgelog.smspush.ui.state.SubscriptionState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.math.log

class SubscriptionViewModel : UIStateViewModel<SubscriptionState>(SubscriptionState()) {

    val user = SmsApplication.instance.preferences.user

    var registration: ListenerRegistration? = null
    private val executor = Executors.newScheduledThreadPool(1)


    init {

        viewModelScope.launch {
            user.collect { u ->
                Log.i(TAG, "Receiving user event")

                registration?.let {
                    Log.i(TAG, "Removing old event listener")
                    it.remove()
                }
                if (u == null) {
                    updateState(uiState.value.copy(messages = listOf()))
                } else {
//                    updateState(uiState.value.copy(user = u))
                    Log.i(TAG, "Registering event listener")
                    registration = Firestore.bucket.addSnapshotListener(executor) { value, error ->
                        Log.i(TAG, "Received event ${value?.documents?.map { it.id }}, $error")
                        if (value == null || value.isEmpty) {
                            updateState(uiState.value.copy(messages = listOf()))
                        } else {
                            try {
                                val messages = value.toObjects(LocalSmsMessage::class.java).map { it.smsValue() }
                            val updateData = uiState.value.copy(
                                messages = messages
                            )
                            Log.i(TAG, "Received event value ${updateData}")
                            updateState(updateData)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error receiving sms data", e)
                            }
                        }
                    }
                }
            }
        }
    }
}