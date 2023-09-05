package com.thehedgelog.smspush.ui.state

import com.thehedgelog.smspush.model.LocalSmsMessageValue

data class SubscriptionState(val messages: List<LocalSmsMessageValue> = listOf())

data class Subscription(val phoneNumber: String, val lastMessage: String, val status: ReadStatus)

enum class ReadStatus {
    READ, UNREAD
}
