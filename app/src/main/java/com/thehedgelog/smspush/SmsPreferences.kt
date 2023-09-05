package com.thehedgelog.smspush

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.thehedgelog.smspush.SmsApplication.Companion.TAG
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore("sms")

class SmsPreferences private constructor(context: Context) {

    private val dataStore = context.dataStore

    val user: Flow<FirebaseUser?> = callbackFlow {

        suspend fun emitUser(currentUser: FirebaseUser?) {
            val oldSubscription = getValue(KEY_SUBSCRIPTION_ID)

            if (oldSubscription != currentUser?.uid) set(KEY_SUBSCRIPTION_ID, currentUser?.uid)

            val isSent = trySend(currentUser)
            Log.i(TAG, "Publishing user state, ${currentUser?.phoneNumber}, successful ${isSent.isSuccess}")
        }

//        emitUser(FirebaseAuth.getInstance().currentUser)

        val subscription = AuthStateListener {
            MainScope().launch { emitUser(it.currentUser) }
        }

        FirebaseAuth.getInstance().addAuthStateListener(subscription)
        awaitClose { FirebaseAuth.getInstance().removeAuthStateListener(subscription) }
    }

    operator fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { pref -> pref[key] }
    }

    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return this@SmsPreferences[key].firstOrNull()
    }

    operator fun <T> set(key: Preferences.Key<T>, value: T?) {
        MainScope().launch {
            if (value == null) {
                dataStore.edit { pref -> pref.remove(key) }
            } else {
                dataStore.edit { pref -> pref[key] = value }
            }
        }
    }


    companion object {

        val KEY_TARGET_NUMBER = stringPreferencesKey("target_number")
        val KEY_BROADCASTING_TOGGLE = booleanPreferencesKey("broadcasting_current")
        val KEY_SUBSCRIPTION_ID = stringPreferencesKey("subscription_id")

        fun init(context: Context): SmsPreferences {
            return SmsPreferences(context)
        }
    }

}