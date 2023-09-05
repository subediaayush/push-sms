package com.thehedgelog.smspush.model

data class FCMPayload(val to: String, val notification: Notification) {
    data class Notification(val title: String, val body: String)
}

//{
//    "to": "/topics/news",
//    "notification": {
//    "title": "Breaking News",
//    "body": "New news story available."
//},
//    "data": {
//    "story_id": "story_12345"
//}
//}
