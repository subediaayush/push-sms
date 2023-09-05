package com.thehedgelog.smspush.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.SYSTEM_HIGH_PRIORITY
import android.provider.Telephony
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.thehedgelog.smspush.receiver.SmsReceiver

object SmsReceiverService {

    private var receiver: SmsReceiver? = null

    fun startService(context: Context) {
//        if (receiver == null) {
//            receiver = SmsReceiver().also {
//                val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
//                filter.priority = SYSTEM_HIGH_PRIORITY
//                LocalBroadcastManager.getInstance(context).registerReceiver(it, filter)
//            }
//            Log.i(TAG, "Registered receiver")
//        }
    }

    fun stopService(context: Context) {
//        receiver?.let { LocalBroadcastManager.getInstance(context).unregisterReceiver(it) }
//        Log.i(TAG, "Unregistered receiver")
//        receiver = null
    }


}