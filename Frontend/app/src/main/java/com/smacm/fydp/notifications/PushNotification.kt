package com.smacm.fydp.notifications

data class PushNotification (
    val data: NotificationData,
    val to: String // recipient of push notification
)