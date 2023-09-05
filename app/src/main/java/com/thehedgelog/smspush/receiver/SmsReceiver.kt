package com.thehedgelog.smspush.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.thehedgelog.smspush.SmsApplication
import com.thehedgelog.smspush.SmsApplication.Companion.TAG
import com.thehedgelog.smspush.SmsPreferences
import com.thehedgelog.smspush.firebase.fcm.Fcm
import com.thehedgelog.smspush.model.LocalSmsMessage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    init {
        Log.i(TAG, "Sms Receiver created")
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Sms Received")
        MainScope().launch {
            Log.i(TAG, "Sms Received in")
            val user = FirebaseAuth.getInstance().currentUser
            val isBroadcastEnabled = SmsApplication.instance.preferences[SmsPreferences.KEY_BROADCASTING_TOGGLE].firstOrNull() ?: false && user != null
            Log.i(TAG, "Sms Received, Broadcast enabled: {$isBroadcastEnabled}")

            if (!isBroadcastEnabled) return@launch

            val messages: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            Log.i(TAG, "Sms Received, ${messages.joinTo(StringBuffer()) { it.toString() }}")
            val collection = FirebaseFirestore.getInstance().collection("${user?.uid ?: ""}/messages/list")
            val format = intent.getStringExtra("format");

            messages.map { LocalSmsMessage(it.timestampMillis, Blob.fromBytes(it.pdu), format) }
                .forEach {
                    Fcm.send(user!!.uid, it)
                    collection.add(it)
                }
        }
    }

}