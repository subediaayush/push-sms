package com.thehedgelog.smspush.firebase.fcm

import android.os.Build
import android.telephony.SmsMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.thehedgelog.smspush.model.FCMPayload
import com.thehedgelog.smspush.model.LocalSmsMessage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.Executors


object Fcm {

    val httpClient = OkHttpClient()
    val gson = Gson()
    val url = "https://fcm.googleapis.com/v1/projects/sms-push-d6fd9/messages:send"

    val executor = Executors.newScheduledThreadPool(1)


    fun send(userId: String, sms: LocalSmsMessage) {
        FirebaseAuth.getInstance().getAccessToken(false).addOnSuccessListener(executor) {
            var tokenResult = it as GetTokenResult
            val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SmsMessage.createFromPdu(sms.pdu.toBytes(), sms.format)
            } else {
                SmsMessage.createFromPdu(sms.pdu.toBytes())
            }

            val from = message.originatingAddress
            val body = message.messageBody

            val payload = FCMPayload(
                to = "/topics/$userId",
                notification = FCMPayload.Notification(
                    title = from ?: "",
                    body = body
                )
            )

            val requestBody = gson.toJson(payload).toByteArray()
                .toRequestBody(contentType = "application/json".toMediaType())
            val request = Request.Builder().url(url).post(requestBody)
                .addHeader("Authorization", "Bearer ${tokenResult.token}")
                .addHeader("Content-Type", "application/json; UTF-8")
                .build()

            httpClient.newCall(request = request).execute()
        }


    }

}
