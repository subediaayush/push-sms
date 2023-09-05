package com.thehedgelog.smspush.model

import android.os.Build
import android.telephony.SmsMessage
import com.google.firebase.firestore.Blob

data class LocalSmsMessage(val timestamp: Long = System.currentTimeMillis(), val pdu: Blob = Blob.fromBytes(ByteArray(0)), val format: String? = "")
data class LocalSmsMessageValue(val timestamp: Long, val from: String, val message: String)

fun LocalSmsMessage.smsValue(): LocalSmsMessageValue {
    val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        SmsMessage.createFromPdu(pdu.toBytes(), format)
    } else {
        SmsMessage.createFromPdu(pdu.toBytes())
    }

    return LocalSmsMessageValue(timestamp = timestamp, from = message.originatingAddress ?: "", message = message.messageBody)
}

