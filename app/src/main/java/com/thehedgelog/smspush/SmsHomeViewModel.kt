package com.thehedgelog.smspush

import android.provider.Telephony.Sms
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.thehedgelog.smspush.SmsPreferences.Companion.init
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SmsHomeViewModel(application: SmsApplication) : AndroidViewModel(application) {

    fun verifyPhoneNumber(number: String): Boolean {
        TODO("Not yet implemented")
    }

    fun setTargetPhoneNumber(number: String) {
        preferences[SmsPreferences.KEY_TARGET_NUMBER] = number
    }

    val preferences = application.preferences;

    val targetPhoneNumber = preferences[SmsPreferences.KEY_TARGET_NUMBER].asLiveData()

}
