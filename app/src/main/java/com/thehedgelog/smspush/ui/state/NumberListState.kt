package com.thehedgelog.smspush.ui.state

data class NumberListState(val currentPage: NumberListPageType = NumberListPageType.SUBSCRIPTION, val phoneNumbers: List<String> = listOf(), val messages: Map<String, List<String>> = mapOf())

enum class NumberListPageType {
    SUBSCRIPTION, BROADCAST
}