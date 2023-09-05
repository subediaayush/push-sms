package com.thehedgelog.smspush

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.thehedgelog.smspush.SmsPreferences.Companion.KEY_BROADCASTING_TOGGLE
import com.thehedgelog.smspush.SmsPreferences.Companion.KEY_SUBSCRIPTION_ID
import com.thehedgelog.smspush.SmsPreferences.Companion.init
import com.thehedgelog.smspush.service.SmsReceiverService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SmsApplication : Application() {

    lateinit var preferences: SmsPreferences;

    override fun onCreate() {
        super.onCreate()

        instance = this
        FirebaseApp.initializeApp(this)

        preferences = SmsPreferences.init(this)

        MainScope().launch {
            preferences[KEY_BROADCASTING_TOGGLE].collect(collector = {
                if (it == true) {
                    SmsReceiverService.startService(this@SmsApplication)
                }
            })
        }


    }

    companion object {
        const val TAG = "SmsApplication"

        lateinit var instance: SmsApplication;
    }


}